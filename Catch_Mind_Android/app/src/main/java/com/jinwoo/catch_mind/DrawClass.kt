package com.jinwoo.catch_mind

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.graphics.Bitmap



class DrawClass(context: Context) : View(context) {


    var touchX: Float = 0.0f
    var touchY: Float = 0.0f
    var paintColor: Int = 0x000000

    private lateinit var drawPath: Path
    private lateinit var drawPaint: Paint
    private lateinit var canvasPaint: Paint
    private lateinit var drawCanvas: Canvas
    private lateinit var canvasBitmap: Bitmap

    init {
        setupDrawing()
    }

    private fun setupDrawing() {

        drawPath = Path()
        drawPaint = Paint()
        drawPaint.color = paintColor
        drawPaint.isAntiAlias = true
        drawPaint.setStrokeWidth(50F)
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND
        canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap)
    }

    override fun onDraw(canvas: Canvas) {

        canvas.drawBitmap(canvasBitmap, 0.toFloat(), 0.toFloat(), canvasPaint)
        canvas.drawPath(drawPath, drawPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchX = event.x
        touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> drawPath.moveTo(touchX, touchY)
            MotionEvent.ACTION_MOVE -> drawPath.lineTo(touchX, touchY)
            MotionEvent.ACTION_UP -> {
                drawPath.lineTo(touchX, touchY)
                drawCanvas.drawPath(drawPath, drawPaint)
                drawPath.reset()
            }
            else -> return false
        }

        invalidate()
        return true
    }
}