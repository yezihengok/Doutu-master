

package com.yzi.doutu.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.ImagePresenter;
import com.pizidea.imagepicker.UilImagePresenter;
import com.pizidea.imagepicker.bean.ImageItem;
import com.pizidea.imagepicker.ui.ImagesGridActivity;
import com.yzi.doutu.R;
import com.yzi.doutu.activity.AboutActivity;
import com.yzi.doutu.activity.BaseActivity;
import com.yzi.doutu.activity.HotTemplateActivity;
import com.yzi.doutu.activity.ModifyImgActivity;
import com.yzi.doutu.activity.ModifyPicActivity;
import com.yzi.doutu.activity.MyDIYPicActivity;
import com.yzi.doutu.activity.MyFavoritesActivity;
import com.yzi.doutu.activity.MyThemeFavoritesActivity;
import com.yzi.doutu.activity.TypeTemplateActivity;
import com.yzi.doutu.adapter.FragmentPagerAdapter;
import com.yzi.doutu.interfaces.CommInterface;
import com.yzi.doutu.interfaces.PermissionsInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.ContextUtil;
import com.yzi.doutu.utils.HandlerUtil;
import com.yzi.doutu.utils.SharedUtils;
import com.yzi.doutu.utils.SimpleFileUtils;
import com.yzi.doutu.utils.StringUtils;
import com.yzi.doutu.utils.TopTips;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.OnClickListener;
import static com.yzi.doutu.utils.CommUtil.WEIBA;
import static com.yzi.doutu.utils.CommUtil.isWeiBaopen;
import static com.yzi.doutu.utils.CommUtil.readAndwriteSdCard;
import static com.yzi.doutu.utils.ImageUtils.DOWN_PATH;

/**
 * Created by yzh on 2016/09/25.
 */
public class MainActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener, OnClickListener
        , AndroidImagePicker.OnPictureTakeCompleteListener {

    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton mFloatingActionButton;
    private NavigationView mNavigationView;

    // TabLayout中的tab标题
    private String[] mTitles;
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments;
    // ViewPager的数据适配器
    private FragmentPagerAdapter mViewPagerAdapter;

    private int page = 0;
    AllListFragment allListFragment;
    NewListFragment listFragment;
    HotListFragment hotListFragment;
    RealManFragment realManFragment;
    private String TAG = this.getClass().getSimpleName();

    ImageView header_img;
    ImagePresenter presenter;
    MenuItem qx_item, weiba_item, cache_item;

    boolean isopen = false;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.yzi.doutu.R.layout.activity_my);
        application.addActivity(this);
        context = this;
        ContextUtil.setApplicationContext(this);
        // 初始化各种控件
        initViews();

        checkBall();

//        SimpleFileUtils.delFile(DOWN_PATH, 500, new CommInterface.DoListener() {
//            @Override
//            public void finish(boolean isOk) {
//            }
//        });
    }

    private void checkBall() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            if (CommUtil.hasModule() && !CommUtil.hasEnable()) {
                CommUtil.showDialog(this, "墙裂建议您去勾选斗图开关，以便于QQ微信运行时显示斗图悬浮球~", "下次吧", "我要开启",
                        null, new CommInterface.setClickListener() {
                            @Override
                            public void onResult() {
                                CommUtil.openModule(MainActivity.this);
                            }
                        });
            }

        }
    }

    private void initViews() {
        mDrawerLayout = (DrawerLayout) findViewById(com.yzi.doutu.R.id.id_drawerlayout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(com.yzi.doutu.R.id.id_coordinatorlayout);
        mAppBarLayout = (AppBarLayout) findViewById(com.yzi.doutu.R.id.id_appbarlayout);
        mToolbar = (Toolbar) findViewById(com.yzi.doutu.R.id.id_toolbar);
        mTabLayout = (TabLayout) findViewById(com.yzi.doutu.R.id.id_tablayout);
        mViewPager = (ViewPager) findViewById(com.yzi.doutu.R.id.id_viewpager);
        mFloatingActionButton = (FloatingActionButton) findViewById(com.yzi.doutu.R.id.id_floatingactionbutton);
        mNavigationView = (NavigationView) findViewById(com.yzi.doutu.R.id.id_navigationview);

        mTitles = getResources().getStringArray(com.yzi.doutu.R.array.tab_titles);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();

        Bundle mBundle = new Bundle();
        mBundle.putInt("flag", 0);
        listFragment = new NewListFragment(mTabLayout);
        listFragment.setArguments(mBundle);
        mFragments.add(listFragment);
        hotListFragment=new HotListFragment(mTabLayout);
        realManFragment = new RealManFragment();
        allListFragment = new AllListFragment();

        mFragments.add(hotListFragment);
        mFragments.add(allListFragment);
        mFragments.add(realManFragment);
        presenter = new UilImagePresenter();
        AndroidImagePicker.getInstance().setOnPictureTakeCompleteListener(this);//watching Picture taking

        configViews();
        //initData();
    }


    private void setDrawerLayout() {
        setweiba();
        setFileSize();
    }

    boolean canserach=true;//是否可计算文件夹大小状态(避免频繁查询文件夹大小影响UI)
    /**文件夹大小**/
    String fileSizes;
    private  void setFileSize() {
        if (cache_item != null&&canserach) {
            canserach=false;
            fileSizes= SimpleFileUtils
                    .getAutoFileOrFilesSize(DOWN_PATH, new CommInterface.DoListener() {
                        @Override
                        public void finish(boolean isOk) {
                            canserach=isOk;
                        }
                    });
            cache_item.setTitle("发送的图片缓存：" +fileSizes);
            Log.e("","发送的图片缓存：" +fileSizes);
        }

    }


    private void configViews() {

        // 设置显示Toolbar
        setSupportActionBar(mToolbar);

        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                        //展开的时候刷新
                        setDrawerLayout();
                        isopen = true;
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                        isopen = false;
                    }
                };

        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        //给NavigationView填充顶部区域，也可在xml中使用app:headerLayout="@layout/header_nav"来设置

        View view = LayoutInflater.from(this).inflate(R.layout.header_nav, null);
        header_img = (ImageView) view.findViewById(R.id.header_img);
        header_img.setOnClickListener(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, CommUtil.dip2px(210));
        view.setLayoutParams(lp);
        mNavigationView.addHeaderView(view);

        //mNavigationView.inflateHeaderView(R.layout.header_nav);
        //给NavigationView填充Menu菜单，也可在xml中使用app:menu="@menu/menu_nav"来设置
        mNavigationView.inflateMenu(R.menu.menu_nav);

        weiba_item = mNavigationView.getMenu().getItem(6);
        qx_item = mNavigationView.getMenu().getItem(7);
        cache_item = mNavigationView.getMenu().getItem(8);
        // 自己写的方法，设置NavigationView中menu的item被选中后要执行的操作
        onNavgationViewMenuItemSelected(mNavigationView);

        // 初始化ViewPager的适配器，并设置给它
        mViewPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        //mViewPager.setOffscreenPageLimit(mTitles.length);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(1);

        //设置TabLayout的模式
        mTabLayout.setTabMode(TabLayout.MODE_FIXED); //Tab平分当前页面
        // mTabLayout.setTabMode(MODE_SCROLLABLE); 可超当前页面多个可滑动
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);

        // 设置FloatingActionButton的点击事件
        mFloatingActionButton.setOnClickListener(this);


        //用 Glide 加载圆形图片
        String url=SharedUtils.getString("", MainActivity.this, "icon_img",null);
        if(isEmpty(url)){
            presenter.displayCircleDrawable(R.mipmap.default_head,header_img);
        }else{
            presenter.onPresentCircleImage(header_img,url, 0);
        }

    }

    private void setweiba() {
        if (CommUtil.isWeiBaopen()) {
            weiba_item.setTitle("分享尾巴: 开");
        } else {
            weiba_item.setTitle("分享尾巴: 关");
        }
    }


    boolean show = true;

    /**
     * 设置NavigationView中menu的item被选中后要执行的操作
     *
     * @param mNav
     */
    private void onNavgationViewMenuItemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                String msgString = "";

                switch (menuItem.getItemId()) {
                    case R.id.nav_menu_home:
                        msgString = (String) menuItem.getTitle();
//                        SharedUtils.putString("",MainActivity.this,"icon_img",null);
//                        presenter.onPresentCircleImage(header_img,
//                                SharedUtils.getString("",MainActivity.this,"icon_img",CommUtil.ICON),0);
                        toActivity(HotTemplateActivity.class);
                        break;

                    case R.id.nav_menu_categories:
                        msgString = (String) menuItem.getTitle();
                        Intent intent = new Intent(MainActivity.this, TypeTemplateActivity.class);
                        startActivity(intent);
                        // mDrawerLayout.closeDrawers();
                        break;

                    case R.id.nav_menu_fav:

                        startActivity(new Intent(MainActivity.this, MyFavoritesActivity.class));
                        // mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_menu_favTheme:
                        startActivity(new Intent(MainActivity.this, MyThemeFavoritesActivity.class));
                        break;
                    case R.id.nav_menu_made:

                        startActivity(new Intent(MainActivity.this, MyDIYPicActivity.class));
                        // mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_select_pic:
                        pickPicture("1:1");


                        break;
                    case R.id.nav_menu_feedback:

                        CommUtil.showDialog(context, "-开启可直接分享至QQ空间与朋友圈.\n-关闭时可以分享发送其它不带尾巴.", "我知道了"
                                , null, new CommInterface.setClickListener() {
                                    @Override
                                    public void onResult() {
                                        if (isWeiBaopen()) {
                                            SharedUtils.putBoolean(WEIBA, WEIBA, false);
                                        } else {
                                            SharedUtils.putBoolean(WEIBA, WEIBA, true);
                                        }
                                        setweiba();
                                    }
                                }, null);


                        break;

                    case R.id.nav_menu_setting:

                        if (!CommUtil.getAppOps(MainActivity.this)) {
                            if (show) {
                                CommUtil.showToast("再次点击可跳转 应用详情 勾选【悬浮窗权限】");
                                show = false;
                            } else {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                            Uri.parse("package:" + getPackageName())));
                                } else {
                                    CommUtil.getAppDetailSettingIntent(MainActivity.this);
                                    CommUtil.showToast("部分手机需要到 权限管理 勾选【悬浮窗权限】");
                                }

                            }
                        } else {
                            CommUtil.showToast("不要点我了，已经开启啦~");
                        }

                        break;

                    case R.id.nav_menu_clean:

                            if(!"0B".equals(fileSizes)){
                        String ss = "您确认要清除 " + DOWN_PATH + " 文件夹下所有缓存图片吗?";
                        CommUtil.showDialog(MainActivity.this, ss, "我要清空", "不了",
                                new CommInterface.setClickListener() {
                                    @Override
                                    public void onResult() {
                                        SimpleFileUtils.delFile(DOWN_PATH, 0, new CommInterface.DoListener() {
                                            @Override
                                            public void finish(boolean isOk) {
                                                if (isOk) {

                                                    final String size=SimpleFileUtils.getAutoFileOrFilesSize(DOWN_PATH,null);

                                                        HandlerUtil.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Log.e("","ok---------------");
                                                                cache_item.setTitle("分享的图片缓存：" +size);
                                                            }
                                                        });
                                                }
                                            }
                                        });
                                    }
                                }, null);
                                  }else{
                                CommUtil.showToast("已经清空了~");
                            }
                        break;
                }

                menuItem.setChecked(true);

                return true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        show = true;
        if (!CommUtil.getAppOps(MainActivity.this)) {
            qx_item.setTitle("悬浮窗权限: 未开启");
        } else {
            qx_item.setTitle("悬浮窗权限: 已开启");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.yzi.doutu.R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == com.yzi.doutu.R.id.action_settings) {
            toActivity(AboutActivity.class);
            return true;
        } else if (id == R.id.action_picTake) {

            if(SharedUtils.getBoolean(null,"notice",true)){
                CommUtil.showDialog(context, "请注意选择相册里竖屏拍摄的图片", "不在提示"
                        , "好的", new CommInterface.setClickListener() {
                            @Override
                            public void onResult() {
                                SharedUtils.putBoolean(null, "notice", false);
                                pickPicture("16:9");
                            }
                        }, new CommInterface.setClickListener() {
                            @Override
                            public void onResult() {
                                pickPicture("16:9");
                            }
                        });
            }else{
                pickPicture("16:9");
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * 从相册选图
     * @param tag 1:1为正方形剪裁  16:9比例剪裁
     */
    private void pickPicture(final String  tag) {

        readAndwriteSdCard(context, new PermissionsInterface() {
            public void onPermissionGranted(String... permissions) {
                if(permissions.length>=3) {

                    AndroidImagePicker.getInstance().setSelectMode(AndroidImagePicker.Select_Mode.MODE_SINGLE);
                    AndroidImagePicker.getInstance().setShouldShowCamera(true);
                    final Intent intent = new Intent(context, ImagesGridActivity.class);

                    intent.putExtra("isCrop", true);
                    intent.putExtra("tag", tag);
                    startActivity(intent);
                    //裁剪监听
                    AndroidImagePicker.getInstance().setCropCompleteListener(new AndroidImagePicker.OnCropCompleteListener() {
                        @Override
                        public void cropComplete(Uri fileUri, Bitmap bitmap) {


                            Bundle bundle = new Bundle();
                            bundle.putString("tag", tag);
                            bundle.putParcelable("fileUri", fileUri);
                            if(StringUtils.isNoEmpty(tag)){
                                toActivity(ModifyImgActivity.class,bundle);
                            }else{
                                toActivity(ModifyPicActivity.class,bundle);
                            }
                            //toActivity(ModifyImgActivity.class,bundle);
                            // MainActivity.this.onActivityResult(CROP, RESULT_OK, intent1);
                        }
                    });

                }
            }
        });

    }

    @Override
    public void onPageSelected(int position) {
        mToolbar.setTitle(mTitles[position]);
        page = position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    boolean ok=false;//是否授予了权限
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus&&!ok){
            readAndwriteSdCard(context, new PermissionsInterface() {
                public void onPermissionGranted(String... permissions) {
                    if(permissions!=null) {
                        ok = true;
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // FloatingActionButton的点击事件
            case com.yzi.doutu.R.id.id_floatingactionbutton:
                if (page == 0) {
                    XRecyclerView xRecyclerView = (XRecyclerView) findViewById(R.id.xrecyclerview);
                    xRecyclerView.smoothScrollToPosition(0);//平滑移动
                    listFragment.refersh();
                } else if (page == 1) {
                    RecyclerView xRecyclerView = (RecyclerView) findViewById(R.id.hotrecyclerview);
                    xRecyclerView.smoothScrollToPosition(0);
                    new TopTips(context,0,40).show(mTabLayout, "已返回顶端",2000L);
                } else if (page == 2) {
                    RecyclerView xRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
                    xRecyclerView.smoothScrollToPosition(0);
                    new TopTips(context,0,40).show(mTabLayout, "已返回顶端",2000L);
                }
                break;

            case R.id.header_img:

                readAndwriteSdCard(context, new PermissionsInterface() {
                    @Override
                    public void onPermissionGranted(String... permissions) {
                        if(permissions.length>=3){
                            AndroidImagePicker.getInstance().setSelectMode(AndroidImagePicker.Select_Mode.MODE_SINGLE);
                            AndroidImagePicker.getInstance().setShouldShowCamera(true);
                            startActivityForResult(new Intent(MainActivity.this, ImagesGridActivity.class), 0x123);
                        }

                    }


                });


                break;

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isopen) {
                    mDrawerLayout.closeDrawers();
                } else {
                    finish();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPictureTakeComplete(final String imagePath) {
        Log.v(TAG, "==调用相机拍照回调=="+imagePath);
        presenter.onPresentCircleImage(header_img,imagePath, 0);
        SharedUtils.putString("", MainActivity.this, "icon_img", imagePath);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //目前都是单选择的图片
        if (requestCode == 0x123) {
            List<ImageItem> imageList = AndroidImagePicker.getInstance().getSelectedImages();
            if (imageList.size() > 0) {
                presenter.onPresentCircleImage(header_img, imageList.get(0).getPath(), 0);
                SharedUtils.putString("", MainActivity.this, "icon_img", imageList.get(0).getPath());
            }

        }

//        else if (requestCode == AndroidImagePicker.REQ_CAMERA) {            //拍照图片
//                if (!TextUtils.isEmpty(AndroidImagePicker.getInstance().getCurrentPhotoPath())) {
//                    AndroidImagePicker.galleryAddPic(this, AndroidImagePicker.getInstance().getCurrentPhotoPath());
//                    AndroidImagePicker.getInstance().notifyPictureTaken();//通知执行 onPictureTakeComplete
//                    Log.i(TAG, "通知执行 onPictureTakeComplete");
//                } else {
//                    Log.i(TAG, "didn't save to your path");
//                }
//            }
    }
}
