package com.example.administrator.point

import android.content.Context
import android.graphics.*
import android.view.View
import android.util.DisplayMetrics
import android.view.WindowManager
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.os.Build
import android.support.annotation.RequiresApi


class XfermodesView : View {
    var sModes = arrayOf<Xfermode>()
    var W = 250
    var H = 250
    var ROW_MAX = 4   // number of samples per row
    var mSrcB: Bitmap? = null
    var mDstB: Bitmap? = null
    var mBG: Shader? = null
    var sLabels = arrayOf<String>()

    constructor(context: Context) : super(context) {
        initData()

    }

    private fun initData() {
        //其中Sa全称为Source alpha表示源图的Alpha通道；Sc全称为Source color表示源图的颜色；Da全称为Destination alpha表示目标图的Alpha通道；Dc全称为Destination color表示目标图的颜色，[...,..]前半部分计算的是结果图像的Alpha通道值，“,”后半部分计算的是结果图像的颜色值。
        //效果作用于src源图像区域
        sModes = arrayOf<Xfermode>(
                //所绘制不会提交到画布上
                PorterDuffXfermode(PorterDuff.Mode.CLEAR),
                //显示上层绘制的图像
                PorterDuffXfermode(PorterDuff.Mode.SRC),
                //显示下层绘制图像
                PorterDuffXfermode(PorterDuff.Mode.DST),
                //正常绘制显示，上下层绘制叠盖
                PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),

                //上下层都显示，下层居上显示
                PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
                //取两层绘制交集，显示上层
                PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
                //取两层绘制交集，显示下层
                PorterDuffXfermode(PorterDuff.Mode.DST_IN),
                //取上层绘制非交集部分，交集部分变成透明
                PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),

                //取下层绘制非交集部分，交集部分变成透明
                PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
                //取上层交集部分与下层非交集部分
                PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
                //取下层交集部分与上层非交集部分
                PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
                //去除两图层交集部分
                PorterDuffXfermode(PorterDuff.Mode.XOR),

                //取两图层全部区域，交集部分颜色加深
                PorterDuffXfermode(PorterDuff.Mode.DARKEN),
                //取两图层全部区域，交集部分颜色点亮
                PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
                //取两图层交集部分，颜色叠加
                PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
                //取两图层全部区域，交集部分滤色
                PorterDuffXfermode(PorterDuff.Mode.SCREEN),

                //取两图层全部区域，交集部分饱和度相加
                PorterDuffXfermode(PorterDuff.Mode.ADD),
                //取两图层全部区域，交集部分叠加
                PorterDuffXfermode(PorterDuff.Mode.OVERLAY))


        sLabels = arrayOf("Clear", "Src", "Dst", "SrcOver", "DstOver", "SrcIn", "DstIn", "SrcOut", "DstOut", "SrcATop", "DstATop", "Xor", "Darken", "Lighten", "Multiply", "Screen", "Add", "Overlay")

        var windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (windowManager != null) {
            val display = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(display)//获取手机像素
            this.W = (display.widthPixels - 64) / ROW_MAX //得到矩形
            this.H = this.W
        }

        //取消硬件加速 API 14之后有些函数不支持硬件加速（系统默认开启）
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mSrcB = makeSrc(W, H)//矩形
        mDstB = makeDst(W, H)//圆形

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.WHITE)

        val labelP = Paint(ANTI_ALIAS_FLAG)
        labelP.textAlign = Paint.Align.CENTER

        val paint = Paint()
        paint.isFilterBitmap = false

        canvas?.translate(15f, 35f)

        var x = 0f
        var y = 0f
        for (i in 0 until sModes.size) {
            // draw the border
            paint.style = Paint.Style.STROKE
            paint.shader = null
            canvas?.drawRect(x - 0.5f, y - 0.5f, x + W.toFloat() + 0.5f, y + H + 0.5f, paint)

            // draw the checker-board pattern
            paint.style = Paint.Style.FILL
            paint.shader = mBG
            canvas?.drawRect(x, y, x + W, y + H, paint)

            // 使用离屏绘制
            val sc = canvas?.saveLayer(x, y, x + W, y + H, null)
            canvas?.translate(x, y)
            canvas?.drawBitmap(makeDst(2 * W / 3, 2 * H / 3), 0f, 0f, paint)

            paint.xfermode = sModes[i]
            canvas?.drawBitmap(makeSrc(2 * W / 3, 2 * H/ 3), W.toFloat() / 3, H.toFloat() / 3, paint)
            paint.xfermode = null
            sc?.toInt()?.let { canvas?.restoreToCount(it) }

            // draw the label
            labelP.textSize = 20f
            canvas?.drawText(sLabels[i], x + W / 2, y - labelP.textSize / 2, labelP)

            x += W + 10

            // wrap around when we've drawn enough for one row
            if (i % ROW_MAX === ROW_MAX - 1) {
                x = 0f
                y += H + 30
            }
        }

    }

    // 画圆一个完成的圆
    fun makeDst(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(ANTI_ALIAS_FLAG)

        p.color = -0x33bc
        c.drawOval(RectF(0f, 0f, w.toFloat(), h.toFloat()), p)
        return bm
    }

    // create a bitmap with a rect, used for the "src" image
    // 矩形右下角留有透明间隙
    fun makeSrc(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(ANTI_ALIAS_FLAG)

        p.color = -0x995501
        c.drawRect(0f, 0f, (w * 19 / 20).toFloat(), (h * 19 / 20).toFloat(), p)
        return bm
    }

}