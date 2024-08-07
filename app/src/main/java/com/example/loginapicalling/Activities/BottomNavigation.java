package com.example.loginapicalling.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class BottomNavigation extends AppCompatActivity {

    private ViewPager2 viewPager; // ViewPager for fragment navigation
    private TabLayout tabLayout; // TabLayout for tabs at the top
    private FloatingActionButton fab, fabAddAlarm, fabAddPerson; // Main FAB and sub-FABs
    private TextView addAlarmActionText, addPersonActionText; // Action text for sub-FABs
    private boolean areSubFabsVisible = false; // Flag to track visibility of sub-FABs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Disable night mode for the activity
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_bottom_navigation); // Set the layout

        // Initialize views
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        fab = findViewById(R.id.add_fab);
        fabAddAlarm = findViewById(R.id.add_alarm_fab);
        fabAddPerson = findViewById(R.id.add_person_fab);
        addAlarmActionText = findViewById(R.id.add_alarm_action_text);
        addPersonActionText = findViewById(R.id.add_person_action_text);

        // Initially hide sub-FABs and action texts
        fabAddAlarm.setVisibility(View.GONE);
        fabAddPerson.setVisibility(View.GONE);
        addAlarmActionText.setVisibility(View.GONE);
        addPersonActionText.setVisibility(View.GONE);

        // Set up the ViewPager with fragments
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                // Return the appropriate fragment based on the position
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
                return 4; // Total number of tabs
            }
        });

        // Set up TabLayoutMediator to link TabLayout and ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Inflate custom tab layout
            View tabView = LayoutInflater.from(BottomNavigation.this).inflate(R.layout.custom_tab, null);
            TextView tabText = tabView.findViewById(R.id.tabText);
            ImageView tabIcon = tabView.findViewById(R.id.tabIcon);

            // Set text and icon for each tab
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

            tab.setCustomView(tabView);
        }).attach();

        // Set up tab selection listeners
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Highlight selected tab
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
                // Reset color for unselected tabs
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
                // No action needed on tab reselected
            }
        });

        // Set up click listener for the main FAB
        fab.setOnClickListener(view -> {
            if (!areSubFabsVisible) {
                // Show sub-FABs and action texts
                fabAddAlarm.show();
                fabAddPerson.show();
                addAlarmActionText.setVisibility(View.VISIBLE);
                addPersonActionText.setVisibility(View.VISIBLE);
                areSubFabsVisible = true;
            } else {
                // Hide sub-FABs and action texts
                fabAddAlarm.hide();
                fabAddPerson.hide();
                addAlarmActionText.setVisibility(View.GONE);
                addPersonActionText.setVisibility(View.GONE);
                areSubFabsVisible = false;
            }
        });

        // Set up click listener for the Add Person FAB
        fabAddPerson.setOnClickListener(view ->
                startActivity(new Intent(BottomNavigation.this, QRCodeScannerActivity.class))
        );

        // Set up click listener for the Add Alarm FAB
        fabAddAlarm.setOnClickListener(view ->
                startActivity(new Intent(BottomNavigation.this, BarcodeScannerActivity.class))
        );
    }

    private void showScanOptions() {
        // Show a popup menu with scanning options
        PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.add_fab));
        popupMenu.getMenuInflater().inflate(R.menu.scan_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_qr_code) {
                // Start QR Code Scanner activity
                startActivity(new Intent(BottomNavigation.this, QRCodeScannerActivity.class));
                return true;
            } else if (id == R.id.menu_bar_code) {
                // Start Barcode Scanner activity
                startActivity(new Intent(BottomNavigation.this, BarcodeScannerActivity.class));
                return true;
            }
            return false;
        });

        popupMenu.show(); // Display the popup menu
    }
}
