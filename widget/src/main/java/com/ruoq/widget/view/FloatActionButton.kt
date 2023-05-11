package com.ruoq.widget.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class FloatActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet ?= null,
    defStyle: Int = 0
) : AppCompatImageView(context, attrs,defStyle){

    companion object{
        //动画显示时长,单位毫秒
        private const val ANIM_TIME = 300
    }

//    显示
    fun show(){
        removeCallbacks(hideRunnable)
        postDelayed(showRunnable, (ANIM_TIME * 2).toLong())
    }

    /**
     * 隐藏
     */
    fun hide() {
        removeCallbacks(showRunnable)
        post(hideRunnable)
    }

    /**
     * 显示悬浮球动画
     */
    private val showRunnable : Runnable = Runnable{
        if(visibility == INVISIBLE){
            visibility = VISIBLE
        }
        val valueAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = ANIM_TIME.toLong()
        valueAnimator.addUpdateListener {
            val value:Float = it.animatedValue as Float
            alpha = value
            scaleX = value
            scaleY = value
        }
        valueAnimator.start()
    }

    /**
     * 隐藏悬浮球动画
     */
    private val hideRunnable:Runnable = Runnable {
        if(visibility == INVISIBLE){
            return@Runnable
        }
        val valueAnimator: ValueAnimator = ValueAnimator.ofFloat(1f, 0f)
        valueAnimator.duration = ANIM_TIME.toLong()
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            if(value == 0f){
                visibility = INVISIBLE
            }
            alpha = value
            scaleX = value
            scaleY = value
        }
        valueAnimator.start()
    }
}