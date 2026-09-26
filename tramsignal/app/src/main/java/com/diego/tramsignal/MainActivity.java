package com.diego.tramsignal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().setNavigationBarColor(Color.BLACK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                controller.setSystemBarsBehavior(
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }

        setContentView(new SignalView(this));
    }

    private static final class SignalView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private int state = 0; // 0: horizontal, 1: circle, 2: vertical

        SignalView(Context context) {
            super(context);
            setBackgroundColor(Color.BLACK);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            setFocusable(true);
            setClickable(true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.BLACK);

            final float w = getWidth();
            final float h = getHeight();
            final float cx = w / 2f;
            final float cy = h / 2f;
            final float base = Math.min(w, h);

            final float length = base * 0.44f;
            final float thickness = base * 0.075f;
            final float radius = base * 0.105f;

            if (state == 0) {
                canvas.drawRect(
                    cx - length / 2f,
                    cy - thickness / 2f,
                    cx + length / 2f,
                    cy + thickness / 2f,
                    paint
                );
            } else if (state == 1) {
                canvas.drawCircle(cx, cy, radius, paint);
            } else {
                canvas.drawRect(
                    cx - thickness / 2f,
                    cy - length / 2f,
                    cx + thickness / 2f,
                    cy + length / 2f,
                    paint
                );
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                state = (state + 1) % 3;
                invalidate();
                performClick();
                return true;
            }
            return true;
        }

        @Override
        public boolean performClick() {
            super.performClick();
            return true;
        }
    }
}
