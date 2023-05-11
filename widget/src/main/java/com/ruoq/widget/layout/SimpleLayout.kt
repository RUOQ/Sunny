package com.ruoq.widget.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import java.lang.Integer.max

open class SimpleLayout @JvmOverloads constructor(
    context: Context,
    attrs:AttributeSet ?= null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes){

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var count : Int = childCount
        var maxHeight = 0
        var maxWidth = 0
        var childState = 0

        for(i in 0 until count){
            val child: View = getChildAt(i)
            //被测量的子view 不能是隐藏的
            if(child.visibility != GONE){
                measureChildWithMargins(child, widthMeasureSpec,0 ,
                     heightMeasureSpec, 0)
                val params: MarginLayoutParams = child.layoutParams as MarginLayoutParams
                maxWidth = kotlin.math.max(maxWidth, child.measuredWidth + params.leftMargin + params.rightMargin)
                maxHeight = kotlin.math.max(maxHeight, child.measuredHeight + params.topMargin + params.bottomMargin)
                childState = combineMeasuredStates(childState, child.measuredState)

            }

            maxWidth += (paddingLeft + paddingRight)
            maxHeight += (paddingTop + paddingBottom)

            maxWidth = kotlin.math.max(maxWidth, suggestedMinimumWidth)
            maxHeight = kotlin.math.max(maxHeight, suggestedMinimumHeight)

            //测量自身
            setMeasuredDimension(
                resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec, childState shl MEASURED_HEIGHT_STATE_SHIFT)
            )

        }

    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //遍历子view
        val count :Int = childCount
        for(i in 0 until count){
            val child : View = getChildAt(i)
            val params : MarginLayoutParams = child.layoutParams as MarginLayoutParams
            val left: Int = params.leftMargin + paddingLeft
            val top: Int = params.topMargin + paddingTop
            val right: Int =
                left + child.measuredWidth + paddingRight + params.rightMargin
            val bottom: Int =
                top + child.measuredHeight + paddingBottom + params.bottomMargin
            child.layout(left,top, right, bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams? {
        return MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(params: LayoutParams?): LayoutParams? {
        return MarginLayoutParams(params)
    }

    override fun checkLayoutParams(params: LayoutParams?): Boolean {
        return params is MarginLayoutParams
    }

    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

}