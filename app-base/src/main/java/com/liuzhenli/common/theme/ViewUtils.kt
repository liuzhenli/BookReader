package com.liuzhenli.common.theme

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.ColorInt

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
@Suppress("unused")
object ViewUtils {

    fun removeOnGlobalLayoutListener(v: View, listener: ViewTreeObserver.OnGlobalLayoutListener) {
        v.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    fun setBackgroundCompat(view: View, drawable: Drawable?) {
        view.background = drawable
    }

    fun setBackgroundTransition(view: View, newDrawable: Drawable): TransitionDrawable {
        val transition = DrawableUtils.createTransitionDrawable(view.background, newDrawable)
        setBackgroundCompat(view, transition)
        return transition
    }

    fun setBackgroundColorTransition(view: View, @ColorInt newColor: Int): TransitionDrawable {
        val oldColor = view.background

        val start = oldColor ?: ColorDrawable(view.solidColor)
        val end = ColorDrawable(newColor)

        val transition = DrawableUtils.createTransitionDrawable(start, end)

        setBackgroundCompat(view, transition)

        return transition
    }

    fun showBottomView(vararg views: View) {
        for (view in views) {
            if (view.visibility == View.VISIBLE) {
                return
            }
            if (view.animation != null) {
                view.animation.cancel()
            }
            val showAnimation: ObjectAnimator =
                ObjectAnimator.ofFloat(view, "translationY", view.height.toFloat(), 0.0f)
            showAnimation.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    view.visibility = View.VISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
            })
            showAnimation.setDuration(300).start()
        }
    }

    fun hideBottomView(vararg views: View) {
        for (view in views) {
            if (view.visibility == View.GONE) {
                return
            }
            if (view.animation != null) {
                view.animation.cancel()
            }
            val hideAnimation =
                ObjectAnimator.ofFloat(view, "translationY", 0f, view.height.toFloat())
            hideAnimation.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            hideAnimation.setDuration(300).start()
        }
    }

    fun showTopView(vararg views: View) {
        for (view in views) {
            if (view.visibility == View.VISIBLE) {
                return
            }
            if (view.animation != null) {
                view.animation.cancel()
            }
            val showAnimation =
                ObjectAnimator.ofFloat(view, "translationY", -view.height.toFloat(), 0f)
            showAnimation.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.VISIBLE
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            showAnimation.setDuration(300).start()
        }
    }

    fun hideTopView(vararg views: View) {
        for (view in views) {
            if (view.visibility == View.GONE) {
                return
            }
            if (view.animation != null) {
                view.animation.cancel()
            }
            val hideAnimation =
                ObjectAnimator.ofFloat(view, "translationY", 0f, -view.height.toFloat())
            hideAnimation.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            hideAnimation.setDuration(300).start()
        }
    }
}
