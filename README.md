## SlidingMenuSample

最近做项目的时候需要一个侧滑菜单栏，本来是想用Android自带的控件DrawerLayout + NavigationView来实现侧边栏，但是发现效果好像跟设计的界面不太一样，
Android自带的侧边栏是覆盖在主页内容之上做侧滑的，我需要的界面是和主页内容平滑过渡的效果。
虽然还有其他类似的侧边栏例如ResideMenu（ResideMenu好像不能自定义Menu布局），但是不想浪费时间了，都是要修改源码，还不如修改个简单的。
所以硬着头皮修改了SlidingMenu源码，让它支持沉浸式状态栏。

### 集成SlidingMenu

github上找到 jfeinstein10/SlidingMenu 这个项目的位置，download下来整个project，import Module 这个项目的library

### 初始化SlidingMenu

这一步网上都有介绍，这里就不说了，不管你是继承SlidingActivity，还是直接new SlidingMenu()都可以实现侧边栏效果，最重要的方法都是attachToActivity()，
先上效果图

![image](https://github.com/AndyRenJie/xxx/xxx/xxx/xxx/xxx.gif)

可以看到状态栏是白色的，影响了沉浸式状态栏效果

### attachToActivity

    /**
     * Attaches the SlidingMenu to an entire Activity
     *
     * @param activity   the Activity
     * @param slideStyle either SLIDING_CONTENT or SLIDING_WINDOW
     */
    public void attachToActivity(Activity activity, int slideStyle) {
        attachToActivity(activity, slideStyle, false);
    }

    /**
     * Attaches the SlidingMenu to an entire Activity
     *
     * @param activity         the Activity
     * @param slideStyle       either SLIDING_CONTENT or SLIDING_WINDOW
     * @param actionbarOverlay whether or not the ActionBar is overlaid
     */
    public void attachToActivity(Activity activity, int slideStyle, boolean actionbarOverlay) {
        if (slideStyle != SLIDING_WINDOW && slideStyle != SLIDING_CONTENT) {
            throw new IllegalArgumentException("slideStyle must be either SLIDING_WINDOW or SLIDING_CONTENT");
        }
        if (getParent() != null) {
            throw new IllegalStateException("This SlidingMenu appears to already be attached");
        }
        // get the window background
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowBackground});
        int background = a.getResourceId(0, 0);
        a.recycle();

        switch (slideStyle) {
            case SLIDING_WINDOW:
                mActionbarOverlay = false;
                ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
                ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
                // save ActionBar themes that have transparent assets
                decorChild.setBackgroundResource(background);
                decor.removeView(decorChild);
                decor.addView(this);
                setContent(decorChild);
                break;
            case SLIDING_CONTENT:
                mActionbarOverlay = actionbarOverlay;
                // take the above view out of
                ViewGroup contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
                View content = contentParent.getChildAt(0);
                contentParent.removeView(content);
                contentParent.addView(this);
                setContent(content);
                // save people from having transparent backgrounds
                if (content.getBackground() == null) {
                    content.setBackgroundResource(background);
                }
                break;
        }
    }
  
从attachToActivity的源码中可以看到它有一个actionbarOverlay的参数，意思是是否需要覆盖ActionBar，在这里把它修改为true
    /**
     * Attaches the SlidingMenu to an entire Activity
     *
     * @param activity   the Activity
     * @param slideStyle either SLIDING_CONTENT or SLIDING_WINDOW
     */
    public void attachToActivity(Activity activity, int slideStyle) {
        attachToActivity(activity, slideStyle, true);
    }
    
这里设置为true，还需要将调用的地方参数slideStyle设置为SlidingMenu.SLIDING_CONTENT，然后效果图就是这样

![image](https://github.com/AndyRenJie/xxx/xxx/xxx/xxx/xxx.gif)

可以看到，状态栏和顶部标题栏重叠了，最后就是设置顶部标题栏的paddingTop属性就可以了

### paddingTop

先获取状态栏的高度status_bar_height，这个网上也有例子就不说了，然后就是setPadding()，top值 = 状态栏的高度 + 标题栏的内边距，所以最后的效果图就是这样

![image](https://github.com/AndyRenJie/xxx/xxx/xxx/xxx/xxx.gif)

Thanks！
