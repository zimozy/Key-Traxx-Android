package com.example.keytraxx;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

/**
 * Created by ibgtraining4 on 1/3/18.
 */

public class MainActivity extends FragmentActivity {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
    KeyTraxxPagerAdapter mKeyTraxxPagerAdapter;
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        CarsFragment carsFragment = new CarsFragment();

        fragmentTransaction.add(R.id.fragmentContainer, carsFragment);
        fragmentTransaction.commit();

//        mKeyTraxxPagerAdapter = new KeyTraxxPagerAdapter(getSupportFragmentManager()); // ViewPager and its adapters use support library fragments, so use getSupportFragmentManager
//        mViewPager = findViewById(R.id.pager);
//        mViewPager.setAdapter(mKeyTraxxPagerAdapter);

//        final ActionBar actionBar = getActionBar(); // comes from the theme mentioned in the manifest

//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

//        actionBar.addTab(
//                actionBar.newTab()
//                        .setText("Map")
//                        .setTabListener(new ActionBar.TabListener() {
//                            @Override
//                            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//                                mViewPager.setCurrentItem(0);
//                            }
//
//                            @Override
//                            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//
//                            }
//
//                            @Override
//                            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//
//                            }
//                        })
//        );
//
//        actionBar.addTab(
//                actionBar.newTab()
//                    .setText("Cars")
//                    .setTabListener(new ActionBar.TabListener() {
//                            @Override
//                            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//                                mViewPager.setCurrentItem(1);
//                            }
//
//                            @Override
//                            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//
//                            }
//
//                            @Override
//                            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//
//                            }
//                        }
//                    )
//        );
//
//        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                getActionBar().setSelectedNavigationItem(position);
//            }
//        });
    }
}