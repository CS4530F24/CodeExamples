package com.example.firebasedemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.firebasedemo.ui.theme.FirebaseDemoTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.ActivityScope
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            FirebaseDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var user by remember { mutableStateOf(Firebase.auth.currentUser) }

                    Column {
                        if (user == null) { //show the login stuff only if the user hasn't logged in yet
                            Column {
                                //UI for inputting username and password
                                var email by remember { mutableStateOf("") }
                                var password by remember { mutableStateOf("") }
                                Text("Not logged in")
                                OutlinedTextField(
                                    value = email,
                                    onValueChange = { email = it },
                                    label = { Text("Email") })
                                OutlinedTextField(
                                    value = password,
                                    onValueChange = { password = it },
                                    label = { Text("Password") },
                                    visualTransformation = PasswordVisualTransformation()
                                )

                                Row {
                                    Button(onClick = {
                                        Firebase.auth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(this@MainActivity) { task ->
                                                if (task.isSuccessful) {
                                                    user = Firebase.auth.currentUser
                                                } else {
                                                    //display errors in email field.  Hacky, but simple for this demo
                                                    email = "login failed, try again"
                                                }
                                            }
                                    }) {
                                        Text("Log In")
                                    }
                                    Button(onClick = {
                                        Firebase.auth.createUserWithEmailAndPassword(
                                            email,
                                            password
                                        )
                                            .addOnCompleteListener(this@MainActivity) { task ->
                                                if (task.isSuccessful) {
                                                    user = Firebase.auth.currentUser
                                                } else {
                                                    email = "Create user failed, try again"
                                                    Log.e("Create user error", "${task.exception}")
                                                }
                                            }
                                    }) {
                                        Text("Sign Up")
                                    }
                                }
                            }

                        } else { //User is logged in, show the main content
                            Text("Welcome ${user!!.email} with id: ${user!!.uid}")
                            var dataString by remember { mutableStateOf("") }
                            LaunchedEffect(Unit) {
                                dataString = downloadDocument(Firebase.firestore)
                            }
                            Text("Data string: $dataString")

                            //store a document to a user-private collection in firestore
                            Button(onClick = {
                                val document = mapOf(
                                    "My uid" to user!!.uid,
                                    "name" to "My name!",
                                    "time" to Date()
                                )
                                lifecycleScope.launch {
                                    uploadDocument(user!!.uid, document)
                                }

                            }) {
                                Text("Post data!")
                            }

                            //store a file in a "bucket" for "object storage" (ie files, not structured documents)
                            var downloadedBitmap by remember { mutableStateOf<Bitmap?>(null) }
                            Button(onClick = {
                                val data = generateBitmap()
                                //upload it to firestore object storage
                                lifecycleScope.launch {
                                    if (uploadData(
                                            Firebase.storage.reference,
                                            "${user!!.uid}/picture.png",
                                            data
                                        )
                                    ) {
                                        downloadedBitmap = downladImage(
                                            Firebase.storage.reference,
                                            "${user!!.uid}/picture.png"
                                        )
                                    }

                                }
                            }) { Text("Save Picture") }

                            //download and show the image saved in firestore if possible

                            if (downloadedBitmap != null) {
                                Image(
                                    bitmap = downloadedBitmap!!.asImageBitmap(),
                                    "Downloaded image"
                                )
                            }



                            Button(onClick = {
                                Firebase.auth.signOut()
                                user = null
                            }) {
                                Text("Sign out")
                            }
                        }
                    }
                }
            }
        }
    }


}

suspend fun downladImage(ref: StorageReference, path: String): Bitmap? {
    val fileRef = ref.child(path)
    return suspendCoroutine {
        fileRef.getBytes(10 * 1024 * 1024).addOnSuccessListener { bytes ->
            it.resume(BitmapFactory.decodeByteArray(bytes, 0, bytes.size))
        }.addOnFailureListener { e ->
            Log.e("DOWNLOAD_IMAGE", "Failed to get image $e")
            it.resume(null)
        }
    }
}

suspend fun downloadDocument(db: FirebaseFirestore): String {
    //probably bad to do this in a composable
    //grab a document from a public folder on firebase
    val collection = db.collection("demoCollection")
    return suspendCoroutine {
        collection
            .get()
            .addOnSuccessListener { result ->
                val doc = result.first()
                it.resume("${doc.id} => ${doc.data}")
            }
            .addOnFailureListener { exception ->
                Log.w("Uh oh", "Error getting documents.", exception)
                it.resume("No data")
            }
    }
}

suspend fun uploadDocument(id: String, document: Any) {
    val db = Firebase.firestore
    suspendCoroutine { continuation ->
        db.collection("users/").document(id)
            .set(document)
            .addOnSuccessListener {
                Log.e("UPLOAD", "SUCCESSFUL!")
                continuation.resume(Unit)
            }
            .addOnFailureListener {
                e -> Log.e("UPLOAD", "FAILED!: $e")
                continuation.resume(Unit)
            }
    }
}

//returns whether the upload was successful
suspend fun uploadData(ref: StorageReference, path: String, data: ByteArray): Boolean {
    val fileRef = ref.child(path)
    return suspendCoroutine { continuation ->
        val uploadTask = fileRef.putBytes(data)
        uploadTask
            .addOnFailureListener { e ->
                Log.e("PICUPLOAD", "Failed !$e")
                continuation.resume(false)
            }
            .addOnSuccessListener {
                Log.d("PICUPLOAD", "success")
                continuation.resume(true)
            }
    }
}



fun generateBitmap(): ByteArray {
    //draw a simple picture using a bitmap + canvas
    val bitmap =
        Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(bitmap)
    val paint = Paint()
    paint.color = Color.RED
    canvas.drawCircle(
        Random.nextFloat() * bitmap.width,
        Random.nextFloat() * bitmap.height,
        100f,
        paint
    )
    paint.color = Color.BLUE
    canvas.drawCircle(
        Random.nextFloat() * bitmap.width,
        Random.nextFloat() * bitmap.height,
        150f,
        paint
    )
    val baos = ByteArrayOutputStream()
    //save it into PNG format (in memory, not a file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos)
    return baos.toByteArray() //bytes of the PNG
}