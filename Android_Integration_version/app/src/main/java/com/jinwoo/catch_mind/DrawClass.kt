package com.jinwoo.catch_mind

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.graphics.Bitmap
import com.jinwoo.catch_mind.Application.SocketApplication
import io.socket.client.Socket

class DrawClass(context: Context) : View(context) {

    val socket: Socket = SocketApplication.socket

    val paintColor: Int = Color.parseColor("#000000")
    var touchX = 0f
    var touchY = 0f

    var drawPath: Path? = Path()
    var drawPaint: Paint? = Paint()
    var canvasPaint: Paint? = null
    var drawCanvas: Canvas? = null
    var canvasBitmap: Bitmap? = null

    init {
        setupDrawing()
    }

    fun setColor(color: String, Width: Float){
        drawPaint!!.color = Color.parseColor(color)
        drawPaint!!.strokeWidth = Width
    }

    fun setupDrawing() {
        drawPaint!!.color = paintColor
        drawPaint!!.isAntiAlias = true
        drawPaint!!.strokeWidth = 5f
        drawPaint!!.style = Paint.Style.STROKE
        drawPaint!!.strokeJoin = Paint.Join.ROUND
        drawPaint!!.strokeCap = Paint.Cap.ROUND
        canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(canvasBitmap, 0f, 0f, canvasPaint)
        canvas.drawPath(drawPath, drawPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchX = event.x
        touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                socket.emit("EVENT_SENDER", touchX, touchY, "ACTION_DOWN")
                drawPath!!.moveTo(touchX, touchY)
            }
            MotionEvent.ACTION_MOVE -> {
                socket.emit("EVENT_SENDER", touchX, touchY, "ACTION_MOVE")
                drawPath!!.lineTo(touchX, touchY)
            }
            MotionEvent.ACTION_UP -> {
                socket.emit("EVENT_SENDER", touchX, touchY, "ACTION_UP")
                drawPath!!.lineTo(touchX, touchY)
                drawCanvas!!.drawPath(drawPath, drawPaint)
                drawPath!!.reset()
            }
            else -> return false
        }

        invalidate()
        return true
    }
}