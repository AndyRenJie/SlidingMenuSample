package com.gstarcad.andy.menusample;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends AppCompatActivity {

    private SlidingMenu slidingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolVersion();
        initSlidingMenu();
    }

    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        //Menu所在位置
        slidingMenu.setMode(SlidingMenu.LEFT);
        //全屏触摸有效
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //菜单布局
        slidingMenu.setMenu(R.layout.main_menu);

        slidingMenu.setContent(R.layout.activity_main);
        //菜单宽度
        slidingMenu.setBehindWidth((int) (0.8f * getResources().getDisplayMetrics().widthPixels));
        //阴影宽度
        slidingMenu.setShadowWidth(10);

        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
    }


    /**
     * 判断版本
     */
    private void boolVersion() {
        View statusBarPlaceHolderView = findViewById(R.id.status_bar_placeholder_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight();
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) statusBarPlaceHolderView.getLayoutParams();
            layoutParams.height = statusBarHeight;
            statusBarPlaceHolderView.setLayoutParams(layoutParams);
        } else {
            statusBarPlaceHolderView.setVisibility(View.GONE);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = -1;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
