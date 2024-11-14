package com.example.firebasedemo

import android.graphics.Bitmap
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertArrayEquals
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RunWith(AndroidJUnit4::class)
class FirebaseTests {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            //10.0.2.2 on the device emulator means your laptop
            Firebase.auth.useEmulator("10.0.2.2", 9099)


            val userEmail = "testUser@gmail.com"
            val userPassword = "password12345"

            //make sure we have an account and we're logged int

            runBlocking {
                suspendCoroutine<Unit> { continuation ->
                    Firebase.auth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnSuccessListener {
                            Log.d("scs", "testAuth: ")
                            continuation.resume(Unit)
                        }
                        .addOnFailureListener { err ->
                            Log.d("test", "ignoring duplicate account erro ${err}")
                            continuation.resume(Unit)
                        }
                }
            }
            runBlocking {
                suspendCoroutine<Unit> { continuation ->
                    Log.d("login", "testAuth: logging in")
                    Firebase.auth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnSuccessListener {
                            Log.d("suser", "testAuth: ")
                            continuation.resume(Unit)
                        }
                        .addOnFailureListener { err ->
                            throw Exception(err)
                        }
                }
            }
        }
    }


    @Test
    fun `test image upload and download`() {
        Log.d("USER", "User: ${Firebase.auth.currentUser!!.email} ${Firebase.auth.currentUser!!.uid}")
        val path = "${Firebase.auth.currentUser!!.uid}/picture.png"

        runBlocking {
            val data = generateBitmap()
            uploadData(Firebase.storage.reference, path, data)
            Log.d("upload", "done, start download")
            val downloadedData = downladImage(Firebase.storage.reference, path)!!
            val baos = ByteArrayOutputStream()
            //save it into PNG format (in memory, not a file)
            downloadedData.compress(Bitmap.CompressFormat.PNG, 0, baos)
            val downloadedBytes = baos.toByteArray() //bytes of the PNG
            assertArrayEquals(data, downloadedBytes)
        }

    }

    @Test
    fun `test document upload and download`() {
        runBlocking {
            val document = mapOf("xyz" to 123)
            uploadDocument(Firebase.auth.currentUser!!.uid, document)
            val collection = Firebase.firestore.collection("users")
            val downloaded = suspendCoroutine {
                collection.document(Firebase.auth.currentUser!!.uid)
                    .get()
                    .addOnSuccessListener { result ->
                        val doc = result
                        it.resume("${doc.id} => ${doc.data}")
                    }
                    .addOnFailureListener { exception ->
                        Log.w("Uh oh", "Error getting documents.", exception)
                        it.resume("No data")
                    }
            }
            assertEquals(downloaded, "helloWorld")
        }
    }

}