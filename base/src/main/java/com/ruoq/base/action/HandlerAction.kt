package com.ruoq.base.action

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.core.os.HandlerCompat.postDelayed

interface HandlerAction {
    companion object{
        val HANDLER: Handler = Handler(Looper.getMainLooper())
    }

    /**
     * 获取Handler
     */
    fun getHandler():Handler{
        return HANDLER
    }

    /**
     * 延迟执行
     */
    fun post(runnable:Runnable):Boolean{
        return postDelayed(runnable, 0)
    }

    /**
     * 延迟一段时间执行
     */
    fun postDelayed(runnable:Runnable ,delayMillis:Long):Boolean{
        return postAtTime(runnable, SystemClock.uptimeMillis() + if(delayMillis < 0) 0 else delayMillis)
    }

    /**
     * 在指定时间执行
     */
    fun postAtTime(runnable: Runnable, uptimeMills:Long):Boolean{
        //发送和当前对象相关的消息回调
        return HANDLER.postAtTime(runnable,this,uptimeMills)
    }

    /**
     * 移除单个消息回调
     */
    fun removeCallbacks(runnable:Runnable){
        HANDLER.removeCallbacks(runnable)
    }

    /**
     * 移除全部消息回调
     */
    fun removeCallbacks(){
        HANDLER.removeCallbacksAndMessages(this)
    }
}