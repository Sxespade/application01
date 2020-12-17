package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:

                openMainWindow();

                return true;
            case R.id.navigation_dashboard:


                int currentPos2 = 0;
                FriendsFragment detail2 = FriendsFragment.create(currentPos2);
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.replace(R.id.main_frame, detail2);  // замена фрагмента
                ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft2.commit();

                return true;
            case R.id.navigation_notifications:

                openProfileFragment();

                return true;
        }
        return false;
    };

    private void openProfileFragment() {
        int currentPos3 = 0;
        ProfileFragment detail3 = ProfileFragment.create(currentPos3);
        FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
        ft3.replace(R.id.main_frame, detail3);  // замена фрагмента
        ft3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft3.commit();
    }

    private void openMainWindow() {
        int currentPos1 = 0;
        MainWindowFragment detail1 = MainWindowFragment.create(currentPos1);
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.main_frame, detail1);  // замена фрагмента
        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft1.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (getIntent().getExtras().get("key1") != null) {
            if (getIntent().getExtras().get("key1").toString().equals("value1")) {
                openProfileFragment();
            }
        } else {
            openMainWindow();
        }


        binding.bottomNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}