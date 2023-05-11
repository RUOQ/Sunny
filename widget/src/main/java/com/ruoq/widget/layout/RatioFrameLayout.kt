package com.ruoq.widget.layout

import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.ruoq.widget.R
import java.lang.IllegalArgumentException

class RatioFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?= null,
    defStyleAttr: Int = 0, defStyleRes: Int = 0
    ) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes){

    /**
     * 宽高比例
     */
    private var widthRatio: Float = 0f
    private var heightRatio: Float = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var finalWidthMeasureSpec: Int = widthMeasureSpec
        var finalHeightMeasureSpec: Int = heightMeasureSpec

        if(widthRatio != 0f && heightRatio != 0f){
            val sizeRatio:Float = getSizeRatio()
            val widthSpecMode: Int = MeasureSpec.getMode(finalWidthMeasureSpec)
            val widthSpecSize: Int = MeasureSpec.getSize(finalWidthMeasureSpec)
            val heightSpecMode: Int = MeasureSpec.getMode(finalHeightMeasureSpec)
            val heightSpecSize: Int = MeasureSpec.getSize(finalHeightMeasureSpec)

            /**
             * 一般情况下LayoutParams.Wrap_content
             * 对应着 MeasureSpec.AT_MOST(自适应),
             * 但是由于我们在代码中强制修改了测量模式为MeasureSpec.EXACTLY(固定值)
             * 这样会有可能重新触发一次onMeasure()
             * 这个时候传入的测量模式就不是MeasureSpec.AT_MOST(自适应)模式
             * 而是MeasureSpec.EXACTLY(固定值)
             * 所以我们要进行双重判断，首先判断LayoutParams
             * 再判断测量模式，这样就能避免因为修改了测量模式触发对宽高的重新计算
             * 最终导致计算结果和上次计算的不同
             */
            if((layoutParams.width != LayoutParams.WRAP_CONTENT)
                && (layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT)
                && (widthSpecMode == MeasureSpec.EXACTLY)
                && (heightSpecMode == MeasureSpec.EXACTLY)
            ){
                //如果当前宽度和高度都是写死的
                if(widthSpecSize / sizeRatio <= heightSpecMode){
                    finalHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                        (widthSpecSize / sizeRatio).toInt(),
                        MeasureSpec.EXACTLY
                    )
                }else if(heightSpecSize * sizeRatio <= widthMeasureSpec){
                    //如果高度经过比例转换不超过原来的宽度
                    finalWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                        (heightSpecSize * sizeRatio).toInt(),
                        MeasureSpec.EXACTLY
                    )
                }
            }else if((layoutParams.width != LayoutParams.WRAP_CONTENT)
                && (widthSpecMode == MeasureSpec.EXACTLY)
                && (heightSpecMode != MeasureSpec.EXACTLY)){
                //如果当前宽度是写死的，但是高度不写死
                finalHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    (widthSpecSize / sizeRatio).toInt(),
                    MeasureSpec.EXACTLY
                )
            }else if((layoutParams.height != LayoutParams.WRAP_CONTENT)
                && (heightSpecMode == MeasureSpec.EXACTLY)
                && (widthSpecMode != MeasureSpec.EXACTLY)){
                //如果当前高度是写死的，但是宽度不写死
                finalWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    (heightSpecSize * sizeRatio).toInt(),
                    MeasureSpec.EXACTLY
                )
            }
        }
        super.onMeasure(finalWidthMeasureSpec, finalHeightMeasureSpec)
    }

    fun getWidthRatio():Float{
        return widthRatio
    }

    fun getHeightRatio():Float{
        return heightRatio
    }

    /**
     * 获取宽高比
     */
    fun getSizeRatio():Float{
        return widthRatio / heightRatio
    }

    /**
     * 设置宽高比
     */
    fun setSizeRatio(widthRatio:Float, heightRatio: Float){
        this.widthRatio = widthRatio
        this.heightRatio = heightRatio
        requestLayout()
        invalidate()
    }

    init{
        val array : TypedArray = context
            .obtainStyledAttributes(attrs, R.styleable.RatioFrameLayout)
        val sizeRatio:String ?= array
            .getString(R.styleable.RatioFrameLayout_sizeRatio)
        if(!TextUtils.isEmpty(sizeRatio)){
            val arrays: Array<String> = sizeRatio!!.split(":").toTypedArray()
            when(arrays.size){
                1 ->{
                    widthRatio = arrays[0].toFloat()
                    heightRatio = 1f
                }
                2 ->{
                    widthRatio = arrays[0].toFloat()
                    heightRatio = arrays[1].toFloat()
                }
                else ->{
                    throw IllegalArgumentException("width or height ratio errors")
                }

            }
            array.recycle()
        }
    }
}