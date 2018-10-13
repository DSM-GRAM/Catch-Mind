package com.jinwoo.catch_mind

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.graphics.Bitmap
import com.jinwoo.catch_mind.Application.SocketApplication
import io.socket.emitter.Emitter


class AutoDrawClass(context: Context) : View(context) {

    var socket: io.socket.client.Socket

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
        socket = SocketApplication.get()
        socket.connect()

        socket.on("EVENT_Receiver", event)
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

    var event = Emitter.Listener{ args ->
        touchX = args[0].toString().toFloat()
        touchY = args[1].toString().toFloat()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                socket.emit("ACTION_DOWN", touchX, touchY)
                drawPath!!.moveTo(touchX, touchY)
            }
            MotionEvent.ACTION_MOVE -> {
                socket.emit("ACTION_MOVE", touchX, touchY)
                drawPath!!.lineTo(touchX, touchY)
            }
            MotionEvent.ACTION_UP -> {
                socket.emit("ACTION_UP", touchX, touchY)
                drawPath!!.lineTo(touchX, touchY)
                drawCanvas!!.drawPath(drawPath, drawPaint)
                drawPath!!.reset()
            }
            else -> false
        }

        invalidate()
        true
    }

}