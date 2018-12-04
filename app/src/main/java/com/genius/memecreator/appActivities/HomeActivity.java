package com.genius.memecreator.appActivities;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.genius.memecreator.R;
import com.genius.memecreator.appAdapters.MyPagerAdapter;
import com.genius.memecreator.appDialogs.EditNewBottomSheetDialog;
import com.genius.memecreator.appFragments.BackgroundsFragment;
import com.genius.memecreator.appFragments.CategoriesFragment;
import com.genius.memecreator.appFragments.HomeFragment;
import com.genius.memecreator.appFragments.MakeCollageFragment;
import com.genius.memecreator.appFragments.ReportAProblemFragment;
import com.genius.memecreator.appFragments.TemplatesFragment;
import com.genius.memecreator.appInterfaces.FragmentRefreshser;
import com.genius.memecreator.appUtils.Keys;
import com.genius.memecreator.appViews.NoSwipeViewPager;

import java.util.List;

import butterknife.BindView;

public class HomeActivity extends SuperCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.no_swipe_view_pager)
    NoSwipeViewPager noSwipeViewPager;

    private long backPressed = 0;

    private HomeFragment homeFragment;
    private TemplatesFragment templatesFragment;
    private ReportAProblemFragment reportAProblemFragment;
    private CategoriesFragment categoriesFragment;
    private MakeCollageFragment makeCollageFragment;
    private BackgroundsFragment backgroundsFragment;

    private MyPagerAdapter myPagerAdapter;
    private EditNewBottomSheetDialog editNewBottomSheetDialog;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (homeFragment != null && homeFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_HOME, homeFragment);
        }

        if (templatesFragment != null && templatesFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_TEMPLATES, templatesFragment);
        }

        if (reportAProblemFragment != null && reportAProblemFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_REPORT_PROBLEM, reportAProblemFragment);
        }

        if (categoriesFragment != null && categoriesFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_CATEGORIES, categoriesFragment);
        }

        if(makeCollageFragment != null && makeCollageFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_COLLAGE, makeCollageFragment);
        }

        if(backgroundsFragment != null && backgroundsFragment.isAdded()) {
            fragmentManager.putFragment(outState, Keys.FRAGMENT_BACKGROUND, backgroundsFragment);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        loadFragments(savedInstanceState);

        editNewBottomSheetDialog = new EditNewBottomSheetDialog(this, new EditNewBottomSheetDialog.EditNewDialogCallback() {
            @Override
            public void onCameraClicked() {
                showToast(HomeActivity.this, "Camera");
            }

            @Override
            public void onGalleryClicked() {
                showToast(HomeActivity.this, "Gallery");
            }

            @Override
            public void onDismissed() {
                showToast(HomeActivity.this, "On Dismissed");
            }
        });
    }

    private void loadFragments(Bundle savedInstanceState) {
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        if (savedInstanceState != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            homeFragment = (HomeFragment) fragmentManager.getFragment(savedInstanceState, Keys.FRAGMENT_HOME);
            categoriesFragment = (CategoriesFragment) fragmentManager.getFragment(savedInstanceState, Keys.FRAGMENT_CATEGORIES);
            templatesFragment = (TemplatesFragment) fragmentManager.getFragment(savedInstanceState, Keys.FRAGMENT_TEMPLATES);
            backgroundsFragment = (BackgroundsFragment) fragmentManager.getFragment(savedInstanceState, Keys.FRAGMENT_BACKGROUND);
            reportAProblemFragment = (ReportAProblemFragment) fragmentManager.getFragment(savedInstanceState, Keys.FRAGMENT_REPORT_PROBLEM);
        }

        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }

        if (categoriesFragment == null) {
            categoriesFragment = new CategoriesFragment();
        }

        if (reportAProblemFragment == null) {
            reportAProblemFragment = new ReportAProblemFragment();
        }

        if (templatesFragment == null) {
            templatesFragment = new TemplatesFragment();
        }

        if(makeCollageFragment == null) {
            makeCollageFragment = new MakeCollageFragment();
        }

        if(backgroundsFragment == null) {
            backgroundsFragment = new BackgroundsFragment();
        }

        myPagerAdapter.addFragment(Keys.FRAGMENT_HOME, homeFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_TEMPLATES, templatesFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_CATEGORIES, categoriesFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_COLLAGE, makeCollageFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_BACKGROUND, backgroundsFragment);
        myPagerAdapter.addFragment(Keys.FRAGMENT_REPORT_PROBLEM, reportAProblemFragment);

        homeFragment.setFragmentRefreshser(new FragmentRefreshser() {
            @Override
            public void onRefresh() {
                homeFragment.refreshHomeFragment();
            }
        });

        noSwipeViewPager.setOffscreenPageLimit(4);
        noSwipeViewPager.setAdapter(myPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        } else {
            if (backPressed == 0) {
                backPressed = System.currentTimeMillis();
                showToast(this, getString(R.string.info_exit));
            } else if (backPressed <= 3000) {
                backPressed = 0;
                super.onBackPressed();
            } else {
                onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                goTo(this, EditorActivity.class, false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                setCurrentView(0);
                break;
            case R.id.templates:
                setCurrentView(1);
                break;
            case R.id.categories:
                setCurrentView(2);
                break;
            case R.id.make_collage:
                setCurrentView(3);
                break;
            case R.id.make_background:
                setCurrentView(4);
                break;
            case R.id.report_problem:
                setCurrentView(5);
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setCurrentView(int item) {
        noSwipeViewPager.setCurrentItem(item, false);
    }

    private void showEditNewDialog() {
        if (editNewBottomSheetDialog != null && !isFinishing()) {
            if (!editNewBottomSheetDialog.isShowing()) {
                editNewBottomSheetDialog.show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        performOnResumeOperations();

    }

    private void performOnResumeOperations() {

        if (noSwipeViewPager.getCurrentItem() > 0) {
            setCurrentView(0);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshFragments();
            }
        }, 1000);
    }

    private void refreshFragments() {
        Fragment currentFragment = getVisibleFragment();

        if (currentFragment instanceof HomeFragment) {
            homeFragment.refreshHomeFragment();
        }
    }

    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            if (fragment != null && fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }
}
