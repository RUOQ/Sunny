package com.ruoq.widget.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CountDownView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?= null,
    defStyleAttr: Int = 0
    ) : AppCompatTextView(context, attrs, defStyleAttr),Runnable{

        companion object{
            /**
             * 秒数单位文本
             */
            private const val TIME_UNIT : String = "S"
        }
    /** 倒计时秒数 **/
    private var totalSecond: Int = 60

    /**
     * 当前秒数
     */
    private var currentSecond:Int = 0

    /** 记录原有的文本 **/
    private var recodeText: CharSequence ?= null

    /**
     * 设置倒计时总秒数
     */
    fun setTotalTime(totalTime: Int){
        totalSecond = totalTime
    }

    /**
     * 开始倒计时
     */
    fun start(){
        recodeText = text
        isEnabled = false
        currentSecond = totalSecond
        post(this)
    }

    fun stop(){
        currentSecond = 0
        text = recodeText
        isEnabled = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        //移除延迟任务，避免内存泄露
        removeCallbacks(this)
    }

    override fun run() {
       if(currentSecond == 0){
           stop()
           return
       }
        currentSecond--
        text = "$currentSecond $TIME_UNIT"
        postDelayed(this,1000)
    }
}