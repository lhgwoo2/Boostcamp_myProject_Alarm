package com.boostcamp.sentialarm;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.boostcamp.sentialarm.Util.BaseActivity;
import com.boostcamp.sentialarm.fragment.HelpDialogFragmentAdapter;
import com.bumptech.glide.Glide;

public class HelpActivity extends BaseActivity {

    private ViewPager viewPager;
    private HelpDialogFragmentAdapter helpDialogFragmentAdapter;
    private TabLayout mTabLayout;

    private ImageView helpBackImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getViewPager();
        setAdapter();

        helpBackImageView = (ImageView) findViewById(R.id.help_background_iv);

        viewPager.setAdapter(helpDialogFragmentAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.help_tablayout);
        mTabLayout.setupWithViewPager(viewPager);
        setupTabname();

        Glide.with(this).load(R.drawable.bg_main_background_6).into(helpBackImageView);

    }

    private void setupTabname() {
        mTabLayout.getTabAt(0).setText("앱 정보");
        mTabLayout.getTabAt(1).setText("라이센스");
    }

    private void setAdapter() {
        if (helpDialogFragmentAdapter == null) {
            helpDialogFragmentAdapter = new HelpDialogFragmentAdapter(getSupportFragmentManager());

        }
    }

    public ViewPager getViewPager() {
        if (null == viewPager) {
            viewPager = (ViewPager) findViewById(R.id.help_viewpager);
        }
        return viewPager;
    }
}
