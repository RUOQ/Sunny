package com.ruoq.widget.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.ruoq.widget.R


/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2021/04/18
 *    desc   : 支持限定 Drawable 大小的 TextView
 */
class DrawableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?= null,
    defStyleAttr:Int = 0
    ) : AppCompatTextView(context, attrs, defStyleAttr){
      private var drawableWidth: Int
      private var drawableHeight:Int

      init{
          val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView)
          drawableWidth = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableWidth, 0)
          drawableHeight = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableHeight, 0)
          array.recycle()
          refreshDrawablesSize()
      }

    /**
     * 刷新Drawable列表的大小
     */
    private fun refreshDrawablesSize() {
       if(drawableWidth == 0 || drawableHeight == 0){
           return
       }
        var compoundDrawables: Array<Drawable?>  = compoundDrawables
        if(compoundDrawables[0] != null || compoundDrawables[1] != null){
            super.setCompoundDrawables(
                limitDrawableSize(compoundDrawables[0]), limitDrawableSize(compoundDrawables[1]),
                limitDrawableSize(compoundDrawables[2]), limitDrawableSize(compoundDrawables[3])
            )
            return
        }

        compoundDrawables = compoundDrawablesRelative
        super.setCompoundDrawables(
            limitDrawableSize(compoundDrawables[0]), limitDrawableSize(compoundDrawables[1]),
            limitDrawableSize(compoundDrawables[2]), limitDrawableSize(compoundDrawables[3])
        )
        return
    }

    /**
     * 重新限定宽高
     */
    private fun limitDrawableSize(drawable:Drawable?):Drawable?{
        if(drawable == null){
            return null
        }
        if(drawableWidth == 0 || drawableHeight == 0){
            return drawable
        }
        drawable.setBounds(0,0 , drawableWidth, drawableHeight)
        return drawable
    }
}