package com.example.box2ddemo

import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.minus
import com.example.box2ddemo.ui.theme.Box2DDemoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val wrapper = NativeWrappers(arrayOf(
            Ball(0f,4f,0.5f),
            Ball(-2f, 5f, .75f),
            Ball(-1f, 7f, 1.0f)))
        setContent {
            Box2DDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var ballData by remember { mutableStateOf(wrapper.balls) }
                    var frame by remember { mutableStateOf(0) }
                    LaunchedEffect(key1 = null){
                        launch {
                            while(true) {
                                wrapper.step()
//                                text = "${wrapper.ballHeight}"
                                delay((1000 / 60.0f).toLong())
                                ballData = wrapper.balls
                                Log.e("BALL HEIGHT", ballData[0].y.toString())
                                frame++
                            }
                        }
                    }
                    Canvas(modifier = Modifier.fillMaxSize()){
                        if(frame > 0) { //trick into redrawing
                            val ppu = pixelsPerUnit(10f, size.width)
                            val originOnScreen = PointF(size.width/2, size.height*.8f)
                            for (ball in ballData) {
                                val scaledRadius = ball.radius * ppu
                                val pixelPosition = worldToScreen(PointF(ball.x, ball.y), originOnScreen, ppu)
                                drawCircle(
                                    Color.Blue, radius = scaledRadius,
                                    Offset(pixelPosition.x, pixelPosition.y)
                                )
                            }
                        }
                    }
                    Text("${ballData[0].x} ${ballData[0].y}")
                }
            }
        }
    }

    companion object {
        init {
            System.loadLibrary("box2ddemo")
        }
    }
}

fun pixelsPerUnit(widthInMeters: Float, widthInPixels:Float) = widthInPixels/widthInMeters

fun worldToScreen(worldPoint: PointF, originOnScreen: PointF, pixelsPerUnit: Float): PointF {
    worldPoint.x *= -pixelsPerUnit
    worldPoint.y *= pixelsPerUnit
    return originOnScreen.minus(worldPoint)
}



