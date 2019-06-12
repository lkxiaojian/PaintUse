package com.example.administrator.point

import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.MotionEvent
import android.view.View
import android.graphics.Canvas.ALL_SAVE_FLAG
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode


/**
 * 刮刮卡效果
 */
class ScratchCard : View {
    var eraser: Bitmap? = null
    var result: Bitmap? = null
    var mPaint: Paint? = null
    var fgBitmap: Bitmap? = null
    var path: Path? = null
    var mEventX: Float = 0.toFloat()
    var mEventY: Float = 0.toFloat()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context) : super(context) {
        result = BitmapFactory.decodeResource(resources, R.drawable.result)//刮奖结果图片
        eraser = BitmapFactory.decodeResource(resources, R.drawable.eraser)//未刮奖结果状态图片
        //禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        path = Path()
        mPaint = Paint()//创建画笔
        mPaint?.style = Paint.Style.STROKE //描边效果
        mPaint?.strokeWidth = 50f
        mPaint?.strokeJoin = Paint.Join.ROUND     //设置连接样式
        mPaint?.strokeCap = Paint.Cap.ROUND      //设置画笔的线帽样式
        fgBitmap = Bitmap.createBitmap(eraser?.width!!, eraser?.height!!, Bitmap.Config.ARGB_8888)

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(result, 0f, 0f, mPaint)//绘制结果
        //使用离屏绘制
        val layerID = canvas?.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), mPaint, ALL_SAVE_FLAG)

        //先将路径绘制到 bitmap上
        val dstCanvas = Canvas(fgBitmap)
        dstCanvas.drawPath(path, mPaint)
        //绘制 目标图像
        canvas?.drawBitmap(fgBitmap, 0f, 0f, mPaint)
        //设置 模式 为 SRC_OUT, 擦橡皮区域为交集区域需要清掉像素
        mPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        //绘制源图像
        canvas?.drawBitmap(eraser, 0f, 0f, mPaint)
        mPaint?.xfermode = null
        layerID?.let { canvas?.restoreToCount(it) }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //用path保留画过的路径
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mEventX = event.x
                mEventY = event.y
                path?.moveTo(mEventX, mEventY)

            }
            MotionEvent.ACTION_MOVE -> {
                var endX = ((event?.x?.minus(mEventX))?.div(2))?.plus(mEventX)
                var endY = (event?.y?.minus(mEventY)) / 2 + mEventY
                //画二阶贝塞尔曲线
                path?.quadTo(mEventX, mEventY, endX, endY)
                mEventX = event.x
                mEventY = event.y
            }

        }
        //触发onDraw事件
        invalidate()
        return true


    }
}