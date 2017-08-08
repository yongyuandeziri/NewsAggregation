package cn.news.ziri.newsaggregation.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.news.ziri.newsaggregation.R;
import cn.news.ziri.newsaggregation.fragment.CloudTagFragment;
import cn.news.ziri.newsaggregation.fragment.HttpFragment;
import cn.news.ziri.newsaggregation.fragment.NewsFragment;
import cn.news.ziri.newsaggregation.sqlite.NewsSourceSQLiteOpenHelper;
import cn.news.ziri.newsaggregation.utils.Logziri;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,CloudTagFragment.OnFragmentInteractionListener {
    private Toolbar toolbar;
    private Fragment mCurrentFragment;
    private SQLiteDatabase Newsource;
    public static List<NewsSource> titles = new ArrayList<NewsSource>();//方便之后动态添加标签，让用户自定义数据源
    public static class NewsSource{
        String name;
        String uri;
        int isNeedJSON;
        int isselected;
    }
    private static boolean isExit = false;
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("新闻聚合");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NewsSourceSQLiteOpenHelper newsourcedb=new NewsSourceSQLiteOpenHelper(this,"newssource.db",null,1);
        Newsource=newsourcedb.getWritableDatabase();
        switchToNews();
    }

    public Cursor GetSelectedNewsSource(){
        String isselectted="1";
        Cursor cursor =Newsource.rawQuery("select * from newssource where isselected=?",new String[]{isselectted});
        return cursor;
    }

    public Cursor GetAllNewsSource(){
        Cursor cursor =Newsource.rawQuery("select * from newssource",new String[]{""});
        return cursor;
    }

    public void UpdateNewsSource(String name){
        Newsource.execSQL("update newsource set isselected=0");//先reset，然后设置可选项
        Newsource.execSQL("update newsource set isselected=1 where name in ?",new String[]{name});
    }

    public void switchToNews() {
        Logziri.d(this.getClass()+"switchToNews");
        toolbar.setTitle("新闻");
        mCurrentFragment=new NewsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, mCurrentFragment).commitAllowingStateLoss();
    }


    public void switchToGitHub(){
    Logziri.d(this.getClass()+"switchToGitHub");
        toolbar.setTitle("GitHub");
        mCurrentFragment=new HttpFragment();
        Bundle bundle=new Bundle();
        bundle.putString("name","GitHub");
        bundle.putString("uri","https://github.com/");
        mCurrentFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,mCurrentFragment).commitAllowingStateLoss();
    }

    public void switchToCSDN(){
        Logziri.d(this.getClass()+"switchToCSDN");
        toolbar.setTitle("CSDN");
        mCurrentFragment=new HttpFragment();
        Bundle bundle=new Bundle();
        bundle.putString("name","CSDN");
        bundle.putString("uri","http://bbs.csdn.net/wap/topics/");
        mCurrentFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,mCurrentFragment).commitAllowingStateLoss();
    }

    public void switchToStackOverFlow(){
        Logziri.d(this.getClass()+"switchToStackOverFlow");
        toolbar.setTitle("StackOverFlow");
        mCurrentFragment=new HttpFragment();
        Bundle bundle=new Bundle();
        bundle.putString("name","StackOverFlow");
        bundle.putString("uri","https://stackoverflow.com/");
        mCurrentFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,mCurrentFragment).commitAllowingStateLoss();
    }

    public void switchTo3DCloud(){
        Logziri.d(this.getClass()+"switchTo3DCloud");
        toolbar.setTitle("修改新闻源");
        mCurrentFragment=new CloudTagFragment().newInstance("","");
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,mCurrentFragment).commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        Logziri.d(getClass()+"onBackPressed");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Logziri.d(getClass()+"onKeydown");
        if(mCurrentFragment instanceof NewsFragment){
            ((NewsFragment)mCurrentFragment).onKeyDown(keyCode);
//            return true;
        }
        else if(mCurrentFragment instanceof HttpFragment)
        {
            ((HttpFragment)mCurrentFragment).onKeyDown(keyCode);//因为每次只有一个活动的fragment
        }
        exit();//按两次回退退出程序
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(mCurrentFragment instanceof NewsFragment){
                switchTo3DCloud();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            switchToNews();//新闻
        } else if (id == R.id.nav_gallery) {
            switchToGitHub();//GitHub
        } else if (id == R.id.nav_slideshow) {
            switchToCSDN();//CSDN
        }else if(id ==R.id.nav_stackoverflow){
            switchToStackOverFlow();
        }
        else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_contact) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Newsource.close();
    }


    private void exit() {
        if (!isExit) {
            isExit = true;
            // 利用handler延迟发送更改状态信息
            View view = findViewById(R.id.drawer_layout);
//            Snackbar.make(view, "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 700);
        } else {
            this.finish();
        }
    }

    @Override
    public void onFragmentInteraction() {
        Logziri.d(getClass()+"onFragmentInteraction");
        switchToNews();
    }

}
