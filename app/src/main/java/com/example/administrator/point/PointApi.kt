package com.example.administrator.point

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.graphics.PorterDuff
import android.graphics.ComposeShader
import android.graphics.Shader
import android.graphics.LinearGradient
import android.graphics.BitmapShader

/**
 * 常用api
 * 图层绘制
 */

class PointApi : View {
    var mPoint: Paint? = null
    var messageLength: Float = 0f
    var mShader:Shader?=null
    var mBitmap:Bitmap?=null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initPoint()
    }

    private fun initPoint() {
        mBitmap=BitmapFactory.decodeResource(resources,R.drawable.beauty)
        mPoint = Paint() // paint 对象
        mPoint?.color = Color.RED //画笔的颜色
        mPoint?.setARGB(255, 0, 255, 0) //设置mPoint对象颜色 范围0 -255
        mPoint?.alpha = 255//设置alpha 透明度  范围 0-255
        mPoint?.isAntiAlias = true //坑锯齿

      /*
        FILL   只绘制图形内容

        STROKE  只绘制图形轮廓（描边）

        FILL_AND_STROKE 既绘制轮廓也绘制内容
        */
        mPoint?.style = Paint.Style.FILL//描边效果
//        mPoint?.strokeWidth=5f //描边宽度
        /**
         * BUTT 结束处 无效果（水平）
         *
         * ROUND 结束处 突出圆角
         *
         * SQUARE 结束处 突出矩阵
         *
         */
        mPoint?.strokeCap=Paint.Cap.BUTT//圆角效果()
        /**
         * MITER  直角
         *
         * ROUND  平滑
         *
         * BEVEL  切割
         */
        mPoint?.strokeJoin=Paint.Join.ROUND //拐角风格
        mPoint?.shader=SweepGradient(1f,1f,Color.BLUE,Color.RED)//设置环形渲染器
        mPoint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.DARKEN) //设置图层混合模式
        mPoint?.colorFilter=LightingColorFilter(0x00ffff, 0x000000)//设置颜色过滤器
        /**
         * 如果该项设置为true，则图像在动画进行中会滤掉对Bitmap图像的优化操作，
         * 加快显示速度，本设置项依赖于dither和xfermode的设置
         */
        mPoint?.isFilterBitmap=true//设置双线过滤
        mPoint?.textSize = 50f
        var message = "Android Point 测试"
        messageLength = mPoint?.measureText(message)!!//获取文本的宽度
        mPoint?.fontMetrics

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val maxWidth = width.toFloat()
        val maxHeight = height.toFloat()
//        canvas?.drawCircle(maxWidth/2,maxHeight/2,150f,mPoint)

//        canvas?.drawText("Android Point 测试", (maxWidth - messageLength) / 2, (maxHeight) / 2, mPoint)


        /**
         * 1.线性渲染,LinearGradient(float x0, float y0, float x1, float y1, @NonNull @ColorInt int colors[], @Nullable float positions[], @NonNull TileMode tile)
         * (x0,y0)：渐变起始点坐标
         * (x1,y1):渐变结束点坐标
         * color0:渐变开始点颜色,16进制的颜色表示，必须要带有透明度
         * color1:渐变结束颜色
         * colors:渐变数组
         * positions:位置数组，position的取值范围[0,1],作用是指定某个位置的颜色值，如果传null，渐变就线性变化。
         * tile:用于指定控件区域大于指定的渐变区域时，空白区域的颜色填充方法
         */
//        mShader = LinearGradient(maxWidth/2,maxHeight/2, 100f, 100f, intArrayOf(Color.RED, Color.BLUE, Color.GREEN), floatArrayOf(0f,0.7f,1f), Shader.TileMode.CLAMP)
//        mPoint?.shader=mShader
////        canvas?.drawCircle(250f, 250f, 250f, mPoint)
//        canvas?.drawRect(0f,0f,500f,500f, mPoint)



        /**
         * 环形渲染，RadialGradient(float centerX, float centerY, float radius, @ColorInt int colors[], @Nullable float stops[], TileMode tileMode)
         * centerX ,centerY：shader的中心坐标，开始渐变的坐标
         * radius:渐变的半径
         * centerColor,edgeColor:中心点渐变颜色，边界的渐变颜色
         * colors:渐变颜色数组
         * stoops:渐变位置数组，类似扫描渐变的positions数组，取值[0,1],中心点为0，半径到达位置为1.0f
         * tileMode:shader未覆盖以外的填充模式。
         */
//        mShader =  RadialGradient(250f, 250f, 250f, intArrayOf(Color.GREEN, Color.YELLOW, Color.RED), null, Shader.TileMode.CLAMP)
//        mPoint?.shader=mShader
//        canvas?.drawCircle(250f, 250f, 250f, mPoint)



        /**
         * 扫描渲染，SweepGradient(float cx, float cy, @ColorInt int color0,int color1)
         * cx,cy 渐变中心坐标
         * color0,color1：渐变开始结束颜色
         * colors，positions：类似LinearGradient,用于多颜色渐变,positions为null时，根据颜色线性渐变
         */
//        mShader =  SweepGradient(250f, 250f, Color.RED, Color.GREEN)
//        mPoint?.shader=mShader
//        canvas?.drawCircle(250f, 250f, 250f, mPoint)

        /**
         * 位图渲染，BitmapShader(@NonNull Bitmap bitmap, @NonNull TileMode tileX, @NonNull TileMode tileY)
         * Bitmap:构造shader使用的bitmap
         * tileX：X轴方向的TileMode
         * tileY:Y轴方向的TileMode
               REPEAT, 绘制区域超过渲染区域的部分，重复排版
               CLAMP， 绘制区域超过渲染区域的部分，会以最后一个像素拉伸排版
               MIRROR, 绘制区域超过渲染区域的部分，镜像翻转排版
       */
//        mShader =  BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
//        mPoint?.shader=mShader
//        canvas?.drawRect(0f,0f,500f, 500f, mPoint)
//        canvas?.drawCircle(250f, 250f, 250f, mPoint)


        /**
         * 组合渲染，
         * ComposeShader(@NonNull Shader shaderA, @NonNull Shader shaderB, Xfermode mode)
         * ComposeShader(@NonNull Shader shaderA, @NonNull Shader shaderB, PorterDuff.Mode mode)
         * shaderA,shaderB:要混合的两种shader
         * Xfermode mode： 组合两种shader颜色的模式
         * PorterDuff.Mode mode: 组合两种shader颜色的模式
         */
        val bitmapShader = BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        val linearGradient = LinearGradient(0f, 0f, 1000f, 1600f, intArrayOf(Color.RED, Color.GREEN, Color.BLUE), null, Shader.TileMode.CLAMP)
        mShader = ComposeShader(bitmapShader, linearGradient, PorterDuff.Mode.MULTIPLY)
        mPoint?.shader=mShader
        mPoint?.isUnderlineText
        canvas?.drawCircle(250f, 250f, 250f, mPoint)
    }

}