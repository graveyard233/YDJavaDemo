package com.lyd.yingdijava.UI.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.lyd.yingdijava.UI.Base.BaseActivity;
import com.lyd.yingdijava.R;


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    @Override
    protected void initViews() {
        drawerLayout = findViewById(R.id.main_drawerLayout);
        toolbar = findViewById(R.id.toolbar);

        ActionBarDrawerToggle barDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.drawer_open,R.string.drawer_close);
        barDrawerToggle.syncState();
        drawerLayout.addDrawerListener(barDrawerToggle);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}