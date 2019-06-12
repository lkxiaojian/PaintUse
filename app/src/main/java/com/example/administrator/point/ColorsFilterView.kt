package com.example.administrator.point

import android.content.Context
import android.graphics.*
import android.provider.CalendarContract
import android.view.View
import android.graphics.ColorMatrixColorFilter
import android.graphics.ColorMatrix


class ColorsFilterView : View {
    var mPaint: Paint? = null
    var mBitmap: Bitmap? = null

    constructor(context: Context) : super(context) {
        initData()
    }

    private fun initData() {
        mPaint = Paint()
        mBitmap = BitmapFactory.decodeResource(resources, R.mipmap.girl)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //#########  LightingColorFilter  #########
        /**
         * R' = R * mul.R / 0xff + add.R
         * G' = G * mul.G / 0xff + add.G
         * B' = B * mul.B / 0xff + add.B
         */
        //红色去除掉
//        var lighting= LightingColorFilter(0x00ffff,0x000000)
//        mPaint?.colorFilter=lighting
//        canvas?.drawBitmap(mBitmap,0f,0f,mPaint)


        //绿色更亮
//        var lighting= LightingColorFilter(0x00ffff,0x003000)
//        mPaint?.colorFilter=lighting
//        canvas?.drawBitmap(mBitmap,0f,0f,mPaint)


        //PorterDuffColorFilter
//        var porterDuffColorFilter=PorterDuffColorFilter(Color.RED,PorterDuff.Mode.DARKEN)
//        mPaint?.colorFilter=porterDuffColorFilter
//        canvas?.drawBitmap(mBitmap,0f,0f,mPaint)


        //  ColorMatrixColorFilter()


        val colorMatrix = floatArrayOf(
                2f, 0f, 0f, 0f, 0f, //red
                0f, 1f, 0f, 0f, 0f, //green
                0f, 0f, 1f, 0f, 0f, //blue
                0f, 0f, 0f, 1f, 0f    //alpha
        )

        val cm = ColorMatrix()
//        //亮度调节
//        cm.setScale(1,2,1,1);

//        //饱和度调节0-无色彩， 1- 默认效果， >1饱和度加强
//        cm.setSaturation(2);

        //色调调节
        cm.setRotate(0, 45f)

        var mColorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
        mPaint?.colorFilter = mColorMatrixColorFilter
        canvas?.drawBitmap(mBitmap, 100f, 0f, mPaint)

    }

}