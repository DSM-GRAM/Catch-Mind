package com.jinwoo.catch_mind

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import io.socket.client.Socket

class DrawClass(context: Context, paintColor: Long) : View(context){

    private var socket: Socket
    lateinit var drawPath: Path
    lateinit var drawPaint: Paint
    lateinit var canvasPaint: Paint
    var paintColor = 0xFF000000
    lateinit var drawCanvas: Canvas
    lateinit var canvasBitmap: Bitmap


    init {
        socket = SocketApplication.get()
        socket.connect()
        this.paintColor = paintColor
        setupDrawing()
    }

    private fun setupDrawing(){

        drawPath = Path()
        drawPaint = Paint()
        drawPaint.setColor(paintColor.toInt())
        drawPaint.setAntiAlias(true)
        drawPaint.setStrokeWidth(30F)
        drawPaint.setStyle(Paint.Style.STROKE)
        drawPaint.setStrokeJoin(Paint.Join.ROUND)
        drawPaint.setStrokeCap(Paint.Cap.ROUND)
        canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = Canvas(canvasBitmap);
    }

    override fun onDraw(canvas: Canvas) {

        canvas.drawBitmap(canvasBitmap, 0.toFloat(), 0.toFloat(), canvasPaint);
        canvas.drawPath(drawPath, drawPaint)
    }

    override fun onTouchEvent (event: MotionEvent): Boolean {
        var touchX: Float = event.getX()
        var touchY: Float = event.getY()

        when (event.getAction()) {

            MotionEvent.ACTION_DOWN -> {
                drawPath.moveTo(touchX, touchY)
                socket.emit("Action down location", touchX, touchY)
            }

            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(touchX, touchY)
                socket.emit("Action move location", touchX, touchY)
            }

            MotionEvent.ACTION_UP -> {
                drawPath.lineTo(touchX, touchY)
                socket.emit("Action up location", touchX, touchY)
                drawCanvas.drawPath(drawPath, drawPaint)
                drawPath.reset()
            }
            else -> return false
        }
        invalidate()
        return true
    }
}