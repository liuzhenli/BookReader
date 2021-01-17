package com.microedu.theme;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * @author Karim Abou Zeid (kabouzeid)
 */
public final class ViewUtil {

    @SuppressWarnings("deprecation")
    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }

    @SuppressWarnings("deprecation")
    public static void setBackgroundCompat(@NonNull View view, @Nullable Drawable drawable) {
        view.setBackground(drawable);
    }

    public static TransitionDrawable setBackgroundTransition(@NonNull View view, @NonNull Drawable newDrawable) {
        TransitionDrawable transition = DrawableUtil.createTransitionDrawable(view.getBackground(), newDrawable);
        setBackgroundCompat(view, transition);
        return transition;
    }

    public static TransitionDrawable setBackgroundColorTransition(@NonNull View view, @ColorInt int newColor) {
        final Drawable oldColor = view.getBackground();

        Drawable start = oldColor != null ? oldColor : new ColorDrawable(view.getSolidColor());
        Drawable end = new ColorDrawable(newColor);

        TransitionDrawable transition = DrawableUtil.createTransitionDrawable(start, end);

        setBackgroundCompat(view, transition);

        return transition;
    }

    private ViewUtil() {
    }

    public static void showTopView(final View... views) {
        for (final View view : views) {
            if (view.getVisibility() == View.VISIBLE) {
                return;
            }
            if (view.getAnimation() != null) {
                view.getAnimation().cancel();
            }
            ObjectAnimator showAnimation = ObjectAnimator.ofFloat(view, "translationY", -view.getHeight(), 0);
            showAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            showAnimation.setDuration(300).start();
        }
    }

    public static void hideTopView(final View... views) {
        for (final View view : views) {
            if (view.getVisibility() == View.GONE) {
                return;
            }
            if (view.getAnimation() != null) {
                view.getAnimation().cancel();
            }
            ObjectAnimator hideAnimation = ObjectAnimator.ofFloat(view, "translationY", 0, -view.getHeight());
            hideAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            hideAnimation.setDuration(300).start();
        }
    }

    public static void showBottomView(final View... views) {
        for (final View view : views) {
            if (view.getVisibility() == View.VISIBLE) {
                return;
            }
            if (view.getAnimation() != null) {
                view.getAnimation().cancel();
            }
            ObjectAnimator showAnimation = ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0);
            showAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            showAnimation.setDuration(300).start();
        }
    }

    public static void hideBottomView(final View... views) {
        for (final View view : views) {
            if (view.getVisibility() == View.GONE) {
                return;
            }
            if (view.getAnimation() != null) {
                view.getAnimation().cancel();
            }
            ObjectAnimator hideAnimation = ObjectAnimator.ofFloat(view, "translationY", 0, view.getHeight());
            hideAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            hideAnimation.setDuration(300).start();
        }
    }


}
