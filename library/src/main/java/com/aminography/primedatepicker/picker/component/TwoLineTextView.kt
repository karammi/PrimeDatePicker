package com.aminography.primedatepicker.picker.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.Paint.Style
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.utils.dp2px
import kotlin.math.max


/**
 * @author aminography
 */
@Suppress("ConstantConditionIf", "MemberVisibilityCanBePrivate", "unused")
internal class TwoLineTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Interior Variables --------------------------------------------------------------------------

    private val dp = context.dp2px(1f)
    private fun dp(value: Float) = dp.times(value).toInt()

    private var firstLabelPaint: Paint? = null
    private var secondLabelPaint: Paint? = null

    private var viewWidth = 0

    // Control Variables ---------------------------------------------------------------------------

    var firstLabelText: String = ""
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var secondLabelText: String = ""
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var firstLabelTextColor: Int = 0
        set(value) {
            field = value
            firstLabelPaint?.color = value
            if (invalidate) invalidate()
        }

    var secondLabelTextColor: Int = 0
        set(value) {
            field = value
            secondLabelPaint?.color = value
            if (invalidate) invalidate()
        }

    var firstLabelTextSize: Int = 0
        set(value) {
            field = value
            firstLabelPaint?.textSize = value.toFloat()
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var secondLabelTextSize: Int = 0
        set(value) {
            field = value
            secondLabelPaint?.textSize = value.toFloat()
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var preferredMinWidth: Int = 0
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    var gapBetweenLines: Int = 0
        set(value) {
            field = value
            if (invalidate) {
                calculateSizes()
                requestLayout()
                invalidate()
            }
        }

    // Programmatically Control Variables ----------------------------------------------------------

    var typeface: Typeface? = null
        set(value) {
            field = value
            applyTypeface()
            if (invalidate) invalidate()
        }

    // ---------------------------------------------------------------------------------------------

    private var invalidate: Boolean = true

    fun doNotInvalidate(function: () -> Unit) {
        val previous = invalidate
        invalidate = false
        function.invoke()
        invalidate = previous
    }

    // ---------------------------------------------------------------------------------------------

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TwoLineTextView, defStyleAttr, defStyleRes).apply {
            doNotInvalidate {

                firstLabelText = getString(R.styleable.TwoLineTextView_firstLabelText) ?: ""
                secondLabelText = getString(R.styleable.TwoLineTextView_secondLabelText) ?: ""

                firstLabelTextColor = getColor(R.styleable.TwoLineTextView_firstLabelTextColor, ContextCompat.getColor(context, R.color.defaultTwoLineTextColor))
                secondLabelTextColor = getColor(R.styleable.TwoLineTextView_secondLabelTextColor, ContextCompat.getColor(context, R.color.defaultTwoLineTextColor))

                firstLabelTextSize = getDimensionPixelSize(R.styleable.TwoLineTextView_firstLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultTwoLineTextSize))
                secondLabelTextSize = getDimensionPixelSize(R.styleable.TwoLineTextView_secondLabelTextSize, resources.getDimensionPixelSize(R.dimen.defaultTwoLineTextSize))

                preferredMinWidth = getDimensionPixelSize(R.styleable.TwoLineTextView_preferredMinWidth, 0)
                gapBetweenLines = getDimensionPixelSize(R.styleable.TwoLineTextView_gapBetweenLines, 0)
            }
            recycle()
        }

        initPaints()
        applyTypeface()
        calculateSizes()

        if (isInEditMode) {
            invalidate()
        }
    }

    private fun calculateSizes() {
        val firstLabelBounds = Rect()
        firstLabelPaint?.getTextBounds(firstLabelText, 0, firstLabelText.length, firstLabelBounds)

        val secondLabelBounds = Rect()
        secondLabelPaint?.getTextBounds(secondLabelText, 0, secondLabelText.length, secondLabelBounds)

        viewWidth = max(firstLabelBounds.width(), secondLabelBounds.width())
        viewWidth = max(viewWidth, preferredMinWidth)
    }

    private fun initFirstLabelPaint() {
        firstLabelPaint = Paint().apply {
            textSize = firstLabelTextSize.toFloat()
            color = firstLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
    }

    private fun initSecondLabelPaint() {
        secondLabelPaint = Paint().apply {
            textSize = secondLabelTextSize.toFloat()
            color = secondLabelTextColor
            style = Style.FILL
            textAlign = Align.CENTER
            isAntiAlias = true
            isFakeBoldText = true
        }
    }

    private fun initPaints() {
        initFirstLabelPaint()
        initSecondLabelPaint()
    }

    private fun applyTypeface() {
        firstLabelPaint?.typeface = typeface
        secondLabelPaint?.typeface = typeface
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = paddingLeft +
            viewWidth +
            paddingRight
        val height = paddingTop +
            firstLabelTextSize +
            gapBetweenLines +
            secondLabelTextSize +
            paddingBottom
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        drawFirstLabel(canvas)
        drawSecondLabel(canvas)
    }

    private fun drawFirstLabel(canvas: Canvas) {
        val x = paddingLeft + viewWidth / 2f
        var y = paddingTop + firstLabelTextSize / 2f

        firstLabelPaint?.apply {
            y -= ((descent() + ascent()) / 2)
        }

        firstLabelPaint?.apply {
            canvas.drawText(
                firstLabelText,
                x,
                y,
                this
            )
        }
    }

    private fun drawSecondLabel(canvas: Canvas) {
        val x = paddingLeft + viewWidth / 2f
        var y = paddingTop + firstLabelTextSize + gapBetweenLines + secondLabelTextSize / 2f

        secondLabelPaint?.apply {
            y -= ((descent() + ascent()) / 2)
        }

        secondLabelPaint?.apply {
            canvas.drawText(
                secondLabelText,
                x,
                y,
                this
            )
        }
    }

    // Save/Restore States -------------------------------------------------------------------------

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)

        savedState.firstLabelText = firstLabelText
        savedState.secondLabelText = secondLabelText
        savedState.firstLabelTextColor = firstLabelTextColor
        savedState.secondLabelTextColor = secondLabelTextColor
        savedState.firstLabelTextSize = firstLabelTextSize
        savedState.secondLabelTextSize = secondLabelTextSize
        savedState.preferredMinWidth = preferredMinWidth
        savedState.gapBetweenLines = gapBetweenLines

        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)

        doNotInvalidate {
            firstLabelText = savedState.firstLabelText
            secondLabelText = savedState.secondLabelText
            firstLabelTextColor = savedState.firstLabelTextColor
            secondLabelTextColor = savedState.secondLabelTextColor
            firstLabelTextSize = savedState.firstLabelTextSize
            secondLabelTextSize = savedState.secondLabelTextSize
            preferredMinWidth = savedState.preferredMinWidth
            gapBetweenLines = savedState.gapBetweenLines
        }

        applyTypeface()
        calculateSizes()
    }

    private class SavedState : BaseSavedState {

        internal var firstLabelText: String = ""
        internal var secondLabelText: String = ""
        internal var firstLabelTextColor: Int = 0
        internal var secondLabelTextColor: Int = 0
        internal var firstLabelTextSize: Int = 0
        internal var secondLabelTextSize: Int = 0
        internal var preferredMinWidth: Int = 0
        internal var gapBetweenLines: Int = 0

        internal constructor(superState: Parcelable?) : super(superState)

        private constructor(input: Parcel) : super(input) {
            firstLabelText = input.readString() ?: ""
            secondLabelText = input.readString() ?: ""
            firstLabelTextColor = input.readInt()
            secondLabelTextColor = input.readInt()
            firstLabelTextSize = input.readInt()
            secondLabelTextSize = input.readInt()
            preferredMinWidth = input.readInt()
            gapBetweenLines = input.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(firstLabelText)
            out.writeString(secondLabelText)
            out.writeInt(firstLabelTextColor)
            out.writeInt(secondLabelTextColor)
            out.writeInt(firstLabelTextSize)
            out.writeInt(secondLabelTextSize)
            out.writeInt(preferredMinWidth)
            out.writeInt(gapBetweenLines)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(input: Parcel): SavedState = SavedState(input)
                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
            }
        }
    }

}