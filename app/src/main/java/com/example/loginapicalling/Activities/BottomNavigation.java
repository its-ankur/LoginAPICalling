package com.example.loginapicalling.Activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.loginapicalling.Fragments.ContactFragment;
import com.example.loginapicalling.Fragments.MyVisitsFragment;
import com.example.loginapicalling.Fragments.SettingsFragment;
import com.example.loginapicalling.Fragments.UserDetailsFragment;
import com.example.loginapicalling.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class BottomNavigation extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_bottom_navigation);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new UserDetailsFragment();
                    case 1:
                        return new MyVisitsFragment();
                    case 2:
                        return new ContactFragment();
                    case 3:
                        return new SettingsFragment();
                    default:
                        return new MyVisitsFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 4; // Adjust if needed
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            View tabView = LayoutInflater.from(BottomNavigation.this).inflate(R.layout.custom_tab, null);
            TextView tabText = tabView.findViewById(R.id.tabText);
            ImageView tabIcon = tabView.findViewById(R.id.tabIcon);

            switch (position) {
                case 0:
                    tabText.setText("Details");
                    tabIcon.setImageResource(R.drawable.baseline_details_24);
                    break;
                case 1:
                    tabText.setText("My Visits");
                    tabIcon.setImageResource(R.drawable.baseline_list_alt_24);
                    break;
                case 2:
                    tabText.setText("Contact");
                    tabIcon.setImageResource(R.drawable.baseline_person_24);
                    break;
                case 3:
                    tabText.setText("Settings");
                    tabIcon.setImageResource(R.drawable.baseline_settings_24);
                    break;
            }

            // Set the custom view for the tab
            tab.setCustomView(tabView);
        }).attach();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                if (tabView != null) {
                    TextView tabText = tabView.findViewById(R.id.tabText);
                    ImageView tabIcon = tabView.findViewById(R.id.tabIcon);
                    tabText.setTextColor(ContextCompat.getColor(BottomNavigation.this, R.color.selected_color));
                    tabIcon.setColorFilter(ContextCompat.getColor(BottomNavigation.this, R.color.selected_color));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                if (tabView != null) {
                    TextView tabText = tabView.findViewById(R.id.tabText);
                    ImageView tabIcon = tabView.findViewById(R.id.tabIcon);
                    tabText.setTextColor(ContextCompat.getColor(BottomNavigation.this, R.color.unselected_color));
                    tabIcon.setColorFilter(ContextCompat.getColor(BottomNavigation.this, R.color.unselected_color));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle reselection if needed
            }
        });

    }

}
