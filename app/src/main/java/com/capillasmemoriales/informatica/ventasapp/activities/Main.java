package com.capillasmemoriales.informatica.ventasapp.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.capillasmemoriales.informatica.ventasapp.R;
import com.capillasmemoriales.informatica.ventasapp.activities.fragments.CalendarFragment;
import com.capillasmemoriales.informatica.ventasapp.activities.fragments.ContactsFragment;
import com.capillasmemoriales.informatica.ventasapp.activities.fragments.ScheduleFragment;
import com.capillasmemoriales.informatica.ventasapp.adapters.TabViewPagerAdapter;

public class Main extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);
        setUpViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager){
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        tabViewPagerAdapter.addFragment(new ContactsFragment(), getString(R.string.contacts));
        tabViewPagerAdapter.addFragment(new CalendarFragment(), getString(R.string.calendar));
        tabViewPagerAdapter.addFragment(new ScheduleFragment(), getString(R.string.schedule));
        viewPager.setAdapter(tabViewPagerAdapter);
    }
}