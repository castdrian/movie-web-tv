package com.castdrian.movie_web

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.MotionEvent.PointerCoords
import android.view.MotionEvent.PointerProperties
import android.view.WindowManager
import android.widget.FrameLayout


class CursorLayout : FrameLayout {
    var EFFECT_DIAMETER = 0
    var EFFECT_RADIUS = 0
    private var callback: Callback? = null

    /* access modifiers changed from: private */
    var cursorDirection = Point(0, 0)

    /* access modifiers changed from: private */
    var cursorHideRunnable = Runnable { this@CursorLayout.invalidate() }

    /* access modifiers changed from: private */
    var cursorPosition = PointF(0.0f, 0.0f)

    /* access modifiers changed from: private */
    var cursorSpeed = PointF(0.0f, 0.0f)
    private val cursorUpdateRunnable: Runnable = object : Runnable {
        override fun run() {
            if (this@CursorLayout.handler != null) {
                this@CursorLayout.handler.removeCallbacks(cursorHideRunnable)
            }
            val currentTimeMillis = System.currentTimeMillis()
            val `access$100` = currentTimeMillis - lastCursorUpdate
            lastCursorUpdate = currentTimeMillis
            val f = `access$100`.toFloat() * 0.05f
            val `access$200` = cursorSpeed
            val cursorLayout = this@CursorLayout
            val f2 = cursorLayout.cursorSpeed.x
            val cursorLayout2 = this@CursorLayout
            val `access$400` = cursorLayout.bound(
                f2 + cursorLayout2.bound(
                    cursorLayout2.cursorDirection.x.toFloat(),
                    1.0f
                ) * f, MAX_CURSOR_SPEED
            )
            val cursorLayout3 = this@CursorLayout
            val f3 = cursorLayout3.cursorSpeed.y
            val cursorLayout4 = this@CursorLayout
            `access$200`[`access$400`] = cursorLayout3.bound(
                f3 + cursorLayout4.bound(
                    cursorLayout4.cursorDirection.y.toFloat(),
                    1.0f
                ) * f, MAX_CURSOR_SPEED
            )
            if (Math.abs(cursorSpeed.x) < 0.1f) {
                cursorSpeed.x = 0.0f
            }
            if (Math.abs(cursorSpeed.y) < 0.1f) {
                cursorSpeed.y = 0.0f
            }
            if (cursorDirection.x.toInt() == 0 && cursorDirection.y == 0 && cursorSpeed.x == 0.0f && cursorSpeed.y == 0.0f) {
                if (this@CursorLayout.handler != null) {
                    this@CursorLayout.handler.postDelayed(cursorHideRunnable, 5000)
                }
                return
            }
            tmpPointF.set(cursorPosition)
            cursorPosition.offset(cursorSpeed.x, cursorSpeed.y)
            Log.d("cursor1234_xxxx", cursorPosition.x.toString())
            Log.d("cursor1234_yyyy", cursorPosition.y.toString())
            if (cursorPosition.x < 0.0f) {
                cursorPosition.x = 0.0f
            } else if (cursorPosition.x > (this@CursorLayout.width - 1).toFloat()) {
                cursorPosition.x = (this@CursorLayout.width - 1).toFloat()
            }
            if (cursorPosition.y < 0.0f) {
                cursorPosition.y = 0.0f
            } else if (cursorPosition.y > (this@CursorLayout.height - 1).toFloat()) {
                cursorPosition.y = (this@CursorLayout.height - 1).toFloat()
            }
            if (tmpPointF != cursorPosition && dpadCenterPressed) {
                val cursorLayout5 = this@CursorLayout
                cursorLayout5.dispatchMotionEvent(
                    cursorLayout5.cursorPosition.x,
                    cursorPosition.y,
                    2
                )
            }
            val childAt = getChildAt(0)
            if (childAt != null) {
                if (cursorPosition.y > (this@CursorLayout.height - SCROLL_START_PADDING).toFloat()) {
                    if (cursorSpeed.y > 0.0f && childAt.canScrollVertically(cursorSpeed.y.toInt())) {
                        childAt.scrollTo(childAt.scrollX, childAt.scrollY + cursorSpeed.y.toInt())
                    }
                } else if (cursorPosition.y < SCROLL_START_PADDING.toFloat() && cursorSpeed.y < 0.0f && childAt.canScrollVertically(
                        cursorSpeed.y.toInt()
                    )
                ) {
                    childAt.scrollTo(childAt.scrollX, childAt.scrollY + cursorSpeed.y.toInt())
                }
                if (cursorPosition.x > (this@CursorLayout.width - SCROLL_START_PADDING).toFloat()) {
                    if (cursorSpeed.x > 0.0f && childAt.canScrollHorizontally(cursorSpeed.x.toInt())) {
                        childAt.scrollTo(childAt.scrollX + cursorSpeed.x.toInt(), childAt.scrollY)
                    }
                } else if (this@CursorLayout.cursorPosition.x < SCROLL_START_PADDING.toFloat() && this@CursorLayout.cursorSpeed.x < 0.0f && childAt.canScrollHorizontally(
                        cursorSpeed.x.toInt()
                    )
                ) {
                    childAt.scrollTo(childAt.scrollX + cursorSpeed.x.toInt(), childAt.scrollY)
                }
            }
            this@CursorLayout.invalidate()
            if (this@CursorLayout.handler != null) {
                this@CursorLayout.handler.post(this)
            }
        }
    }

    /* access modifiers changed from: private */
    var dpadCenterPressed = false

    /* access modifiers changed from: private */
    var lastCursorUpdate = System.currentTimeMillis()
    private val paint = Paint()
    var tmpPointF = PointF()

    interface Callback {
        fun onUserInteraction()
    }

    /* access modifiers changed from: private */
    fun bound(f: Float, f2: Float): Float {
        if (f > f2) {
            return f2
        }
        val f3 = -f2
        return if (f < f3) f3 else f
    }

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attributeSet: AttributeSet?) : super(
        context!!, attributeSet
    ) {
        init()
    }

    private fun init() {
        if (!isInEditMode) {
            paint.isAntiAlias = true
            setWillNotDraw(false)
            val defaultDisplay =
                (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            val point = Point()
            defaultDisplay.getSize(point)
            EFFECT_RADIUS = point.x / 20
            EFFECT_DIAMETER = EFFECT_RADIUS * 2
            CURSOR_STROKE_WIDTH = (point.x / 400).toFloat()
            CURSOR_RADIUS = point.x / 110
            MAX_CURSOR_SPEED = (point.x / 25).toFloat()
            SCROLL_START_PADDING = point.x / 15
        }
    }

    fun setCallback(callback2: Callback?) {
        callback = callback2
    }

    override fun onInterceptTouchEvent(motionEvent: MotionEvent): Boolean {
        val callback2 = callback
        callback2?.onUserInteraction()
        return super.onInterceptTouchEvent(motionEvent)
    }

    /* access modifiers changed from: protected */
    public override fun onSizeChanged(i: Int, i2: Int, i3: Int, i4: Int) {
        super.onSizeChanged(i, i2, i3, i4)
        if (!isInEditMode) {
            cursorPosition[i.toFloat() / 2.0f] = i2.toFloat() / 2.0f
            if (handler != null) {
                handler.postDelayed(cursorHideRunnable, 5000)
            }
        }
    }

    override fun dispatchKeyEvent(keyEvent: KeyEvent): Boolean {
        val callback2 = callback
        callback2?.onUserInteraction()
        val keyCode = keyEvent.keyCode
        if (!(keyCode == 66 || keyCode == 160)) {
            when (keyCode) {
                19 -> {
                    if (keyEvent.action == 0) {
                        if (cursorPosition.y <= 0.0f) {
                            return super.dispatchKeyEvent(keyEvent)
                        }
                        handleDirectionKeyEvent(keyEvent, -100, -1, true)
                    } else if (keyEvent.action == 1) {
                        handleDirectionKeyEvent(keyEvent, -100, 0, false)
                    }
                    return true
                }

                20 -> {
                    if (keyEvent.action == 0) {
                        if (cursorPosition.y >= height.toFloat()) {
                            return super.dispatchKeyEvent(keyEvent)
                        }
                        handleDirectionKeyEvent(keyEvent, -100, 1, true)
                    } else if (keyEvent.action == 1) {
                        handleDirectionKeyEvent(keyEvent, -100, 0, false)
                    }
                    return true
                }

                21 -> {
                    if (keyEvent.action == 0) {
                        if (cursorPosition.x <= 0.0f) {
                            return super.dispatchKeyEvent(keyEvent)
                        }
                        handleDirectionKeyEvent(keyEvent, -1, -100, true)
                    } else if (keyEvent.action == 1) {
                        handleDirectionKeyEvent(keyEvent, 0, -100, false)
                    }
                    return true
                }

                22 -> {
                    if (keyEvent.action == 0) {
                        if (cursorPosition.x >= width.toFloat()) {
                            return super.dispatchKeyEvent(keyEvent)
                        }
                        handleDirectionKeyEvent(keyEvent, 1, -100, true)
                    } else if (keyEvent.action == 1) {
                        handleDirectionKeyEvent(keyEvent, 0, -100, false)
                    }
                    return true
                }

                23 -> {}
                else -> when (keyCode) {
                    268 -> {
                        if (keyEvent.action == 0) {
                            handleDirectionKeyEvent(keyEvent, -1, -1, true)
                        } else if (keyEvent.action == 1) {
                            handleDirectionKeyEvent(keyEvent, 0, 0, false)
                        }
                        return true
                    }

                    269 -> {
                        if (keyEvent.action == 0) {
                            handleDirectionKeyEvent(keyEvent, -1, 1, true)
                        } else if (keyEvent.action == 1) {
                            handleDirectionKeyEvent(keyEvent, 0, 0, false)
                        }
                        return true
                    }

                    270 -> {
                        if (keyEvent.action == 0) {
                            handleDirectionKeyEvent(keyEvent, 1, -1, true)
                        } else if (keyEvent.action == 1) {
                            handleDirectionKeyEvent(keyEvent, 0, 0, false)
                        }
                        return true
                    }

                    271 -> {
                        if (keyEvent.action == 0) {
                            handleDirectionKeyEvent(keyEvent, 1, 1, true)
                        } else if (keyEvent.action == 1) {
                            handleDirectionKeyEvent(keyEvent, 0, 0, false)
                        }
                        return true
                    }
                }
            }
        }
        if (!isCursorDissappear) {
            if (keyEvent.action == 0 && !keyDispatcherState.isTracking(keyEvent)) {
                keyDispatcherState.startTracking(keyEvent, this)
                dpadCenterPressed = true
                dispatchMotionEvent(cursorPosition.x, cursorPosition.y, 0)
            } else if (keyEvent.action == 1) {
                keyDispatcherState.handleUpEvent(keyEvent)
                dispatchMotionEvent(cursorPosition.x, cursorPosition.y, 1)
                dpadCenterPressed = false
            }
            return true
        }
        return super.dispatchKeyEvent(keyEvent)
    }

    /* access modifiers changed from: private */
    fun dispatchMotionEvent(f: Float, f2: Float, i: Int) {
        val uptimeMillis = SystemClock.uptimeMillis()
        val uptimeMillis2 = SystemClock.uptimeMillis()
        val pointerProperties = PointerProperties()
        pointerProperties.id = 0
        pointerProperties.toolType = 1
        val pointerPropertiesArr = arrayOf(pointerProperties)
        val pointerCoords = PointerCoords()
        pointerCoords.x = f
        pointerCoords.y = f2
        pointerCoords.pressure = 1.0f
        pointerCoords.size = 1.0f
        dispatchTouchEvent(
            MotionEvent.obtain(
                uptimeMillis,
                uptimeMillis2,
                i,
                1,
                pointerPropertiesArr,
                arrayOf(pointerCoords),
                0,
                0,
                1.0f,
                1.0f,
                0,
                0,
                0,
                0
            )
        )
    }

    private fun handleDirectionKeyEvent(keyEvent: KeyEvent, i: Int, i2: Int, z: Boolean) {
        var i = i
        var i2 = i2
        lastCursorUpdate = System.currentTimeMillis()
        if (!z) {
            keyDispatcherState.handleUpEvent(keyEvent)
            cursorSpeed[0.0f] = 0.0f
        } else if (!keyDispatcherState.isTracking(keyEvent)) {
            val handler = handler
            handler.removeCallbacks(cursorUpdateRunnable)
            handler.post(cursorUpdateRunnable)
            keyDispatcherState.startTracking(keyEvent, this)
        } else {
            return
        }
        val point = cursorDirection
        if (i == -100) {
            i = point.x
        }
        if (i2 == -100) {
            i2 = cursorDirection.y
        }
        point[i] = i2
    }

    /* access modifiers changed from: protected */
    public override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (!isInEditMode && !isCursorDissappear) {
            val f = cursorPosition.x
            val f2 = cursorPosition.y
            paint.color = Color.argb(128, 255, 255, 255)
            paint.style = Paint.Style.FILL
            canvas.drawCircle(f, f2, CURSOR_RADIUS.toFloat(), paint)
            paint.color = -7829368
            paint.strokeWidth = CURSOR_STROKE_WIDTH
            paint.style = Paint.Style.STROKE
            canvas.drawCircle(f, f2, CURSOR_RADIUS.toFloat(), paint)
        }
    }

    private val isCursorDissappear: Boolean
        private get() = System.currentTimeMillis() - lastCursorUpdate > 5000

    /* access modifiers changed from: protected */
    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    companion object {
        const val CURSOR_DISAPPEAR_TIMEOUT = 5000
        var CURSOR_RADIUS = 0
        var CURSOR_STROKE_WIDTH = 0.0f
        var MAX_CURSOR_SPEED = 0.0f
        var SCROLL_START_PADDING = 100
        const val UNCHANGED = -100
    }
}