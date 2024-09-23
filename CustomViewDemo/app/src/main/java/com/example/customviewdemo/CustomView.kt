package com.example.customviewdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    //OK for this demo, but views are created/destroyed during lifecycle events so
    //this should really be stored in a viewmodel!
    private val bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
    private val bitmapCanvas = Canvas(bitmap)
    private val paint = Paint()

    //width/height are 0 when the constructor is called
    //use the lazy delegated property to initialize it on first access, once the size is set
    private val rect: Rect by lazy {Rect(0,0,width, height)}


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, null, rect, paint)
    }

    //draws to our bitmap.  If the bitmap is stored in a VM, this method should probably get
    //moved to the fragment or possibly the VM
    public fun drawCircle(color: Color){
        paint.color = Color.WHITE
        bitmapCanvas.drawRect(0f,0f, bitmap.width.toFloat(), bitmap.height.toFloat(), paint)
        paint.color = color.toArgb()
        bitmapCanvas.drawCircle(0.5f*bitmap.width, 0.5f*bitmap.height,
            0.25f*bitmap.width, paint)

    }

}