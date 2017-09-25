package com.summertaker.communitywebview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, WebFragment.WebFragmentListener {

    ProgressBar mProgressBar;

    ActionBarDrawerToggle mDrawerToggle;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runFragment("goTop");
            }
        });

        mProgressBar = findViewById(R.id.toolbar_progress_bar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                runFragment("goBack");
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(sectionsPagerAdapter);

        //-------------------------------------------------------------------------------------------------------
        // 뷰페이저 간 이동 시 프레그먼트 자동으로 새로고침 방지
        // https://stackoverflow.com/questions/28494637/android-how-to-stop-refreshing-fragments-on-tab-change
        //-------------------------------------------------------------------------------------------------------
        mViewPager.setOffscreenPageLimit(BaseApplication.getInstance().getSiteList().size());

        SlidingTabLayout slidingTabLayout = findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            runFragment("goBack");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_open_in_new:
                runFragment("open_in_new");
                return true;
            case R.id.action_go_top:
                runFragment("goTop");
                return true;
            case R.id.action_refresh:
                runFragment("refresh");
                return true;
            case R.id.action_share:
                runFragment("share");
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
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return WebFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return BaseApplication.getInstance().getSiteList().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return BaseApplication.getInstance().getSiteData(position).getTitle();
        }
    }

    public void runFragment(String command) {
        //--------------------------------------------------------------------------------------------
        // 프레그먼트에 이벤트 전달하기
        // https://stackoverflow.com/questions/34861257/how-can-i-set-a-tag-for-viewpager-fragments
        //--------------------------------------------------------------------------------------------
        Fragment f = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + mViewPager.getCurrentItem());

        // based on the current position you can then cast the page to the correct Fragment class
        // and call some method inside that fragment to reload the data:
        //if (0 == mViewPager.getCurrentItem() && null != f) {
        if (f == null) {
            if ("goBack".equals(command)) {
                super.onBackPressed();
            }
        } else {
            WebFragment wf = (WebFragment) f;

            switch (command) {
                case "goBack":
                    boolean canGoBack = wf.goBack();
                    if (!canGoBack) {
                        super.onBackPressed();
                    }
                    break;
                case "goTop":
                    wf.goTop();
                    break;
                case "refresh":
                    wf.refresh();
                    break;
                case "open_in_new":
                    wf.openInNew();
                    break;
                case "share":
                    wf.share();
                    break;
            }
        }
    }

    @Override
    public void onWebFragmentEvent(String event, String url, boolean canGoBack) {
        switch (event) {
            case "onPageStarted":
                //mProgressBar.setVisibility(View.VISIBLE);

                // 툴바 햄버거 아이콘을 기본으로 설정
                //mDrawerToggle.setDrawerIndicatorEnabled(true);

                break;
            case "onPageFinished":
                //mProgressBar.setVisibility(View.GONE);

                /*
                // 툴바 햄버거 아이콘을 뒤로 가기 아이콘으로 변경
                if (canGoBack) {
                    mDrawerToggle.setDrawerIndicatorEnabled(false);
                    mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
                    mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            runFragment("goBack");
                        }
                    });
                } else {
                    mDrawerToggle.setDrawerIndicatorEnabled(true);
                }
                */
                break;
        }
    }
}
