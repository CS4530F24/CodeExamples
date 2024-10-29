package com.example.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composedemo.ui.theme.ComposeDemoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            ),
                            title = {
                                Text("Demo Top App Bar")
                            }
                        )
                    },
                    bottomBar = {
                        BottomAppBar {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                text = "My bottom app bar",
                            )
                        }
                    }) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column {
                            //show the row when showTop is true, and scale it/fade it in/out
                            //when shown/hidden
                            DisappearingSection()
                            //NestedBouncing("Android")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NestedBouncing(name: String, modifier: Modifier = Modifier) {
    //loop the animations forever
    val outerInfinite = rememberInfiniteTransition(label = "outerInfinite")
    //this is the fraction of the screen that the box will cover, we'll just interpolate between
    // .2 and 1.0 of the screen size forever
    val outerFraction by outerInfinite.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = EaseInBounce),
            repeatMode = RepeatMode.Reverse
        ),
        label = "outerFraction" //labels show up in the animation editor/debugger
    )
    //hold the internal box
    Box(modifier = modifier.fillMaxSize()) {
        //this is the blue box
        Box(modifier = modifier
            //what we're animating
            .fillMaxSize(fraction = outerFraction)
            .align(Alignment.Center)
            //make the background blue, we could animate the color if we wanted
            .drawBehind {
                drawRect(Color.Blue)
            }
        ) {

            //control where in the box the circle appears, again go between .1 and 1 of the box size
            val innerFraction by outerInfinite.animateFloat(
                initialValue = 0.1f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    tween(800, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "innerFraction"
            )
            //box with constraints lets us access the maxWidth of our parent which is helpful here
            BoxWithConstraints {
                Box(
                    modifier = modifier
                        .offset( //place it
                            maxWidth * innerFraction - 40.dp,
                            maxHeight * innerFraction - 40.dp
                        )
                        .size(80.dp)
                        .clip(CircleShape) //only draw in a circle
                        .drawBehind {
                            drawRect(Color.Green)
                        },
                    contentAlignment = Alignment.Center //put the text in the middle of the circle
                ) {
                    Text(name)
                }
            }

        }
    }
}

@Composable
fun DisappearingSection() {
    var showTop by remember { mutableStateOf(true) }

    //this combo lets us change the state every 2 seconds using
    //delay.  If I wanted to cancel/relaunch the effect when some object changed
    //I could set the key to something besides null
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = null) {
        coroutineScope.launch {
            while (true) {
                delay(2000)
                showTop = !showTop
            }
        }
    }
    AnimatedVisibility(visible = showTop) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.Red)
        ) {
            Text("Here I am!")
        }
    }

}

//if you hover over the top of the "design" window you can access the animation preview
//which shows lots of useful info about the animation
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeDemoTheme {
        NestedBouncing("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun disappearingSectionPreview() {
    ComposeDemoTheme {
        DisappearingSection()
    }
}