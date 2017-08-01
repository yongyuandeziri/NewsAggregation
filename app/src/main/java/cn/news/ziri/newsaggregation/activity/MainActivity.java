package cn.news.ziri.newsaggregation.activity;

import android.os.Bundle;
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

import cn.news.ziri.newsaggregation.R;
import cn.news.ziri.newsaggregation.fragment.GitHubFragment;
import cn.news.ziri.newsaggregation.fragment.NewsFragment;
import cn.news.ziri.newsaggregation.utils.Logziri;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import static android.view.KeyEvent.KEYCODE_BACK;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private Fragment mCurrentFragment;
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

        switchToNews();
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
        mCurrentFragment=new GitHubFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,mCurrentFragment).commitAllowingStateLoss();
    }


    @Override
    public void onBackPressed() {
        Logziri.d(getClass()+"onBackPressed");
        if(mCurrentFragment instanceof GitHubFragment){
            ((GitHubFragment)mCurrentFragment).onKeyDown(KEYCODE_BACK);
        }
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
        if(mCurrentFragment instanceof GitHubFragment){
            ((GitHubFragment)mCurrentFragment).onKeyDown(keyCode);
            return true;
        }
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
//            showShare();//CSDN
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if(id ==R.id.nav_exit){
         //退出
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
