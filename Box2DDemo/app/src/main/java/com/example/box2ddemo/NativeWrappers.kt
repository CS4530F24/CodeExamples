package com.example.box2ddemo

import android.graphics.Point


data class Ball(var x: Float, var y: Float, val radius: Float)
class NativeWrappers(val balls: Array<Ball>) {
    private val cppPointer: Long = initBox2D(balls)
    fun step(){
        step(cppPointer, balls)
    }


    private external fun initBox2D(balls: Array<Ball>): Long
    //pass the "world pointer" and it returns the Y value of the ball
    private external fun step(ptr: Long, balls: Array<Ball>)
    private external fun helloWorld(): Unit

    private external fun newJNIFunction(arr: Array<Int>, s: String, point: Point): Point
}