package com.example.barberchair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerDashboardActivity extends AppCompatActivity {
    FirebaseAuth nAuth;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        actionBar = getSupportActionBar();
        actionBar.setTitle("The Customer");

        nAuth = FirebaseAuth.getInstance();

        //here when the user selects the down buttons
        BottomNavigationView navigationView = findViewById(R.id.navigation2);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        actionBar.setTitle("Home");
        HomeCustomerFragment fragment = new HomeCustomerFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contents,fragment,"");
        fragmentTransaction.commit();
    }
    private  BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.barber_cust:
                    actionBar.setTitle("Home");
                    HomeCustomerFragment fragment1 = new HomeCustomerFragment();
                    FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.contents,fragment1,"");
                    fragmentTransaction1.commit();

                    return  true;

                case R.id.profile_cust:
                    actionBar.setTitle("Profile");
                    UserProfileFragment fragment2 = new UserProfileFragment();
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.contents,fragment2,"");
                    fragmentTransaction2.commit();
                    return  true;
            }
            return false;
        }
    };
    private void checkUser(){
        FirebaseUser user = nAuth.getCurrentUser();

        if (user != null){

        }
        else{
            Intent intent = new Intent(CustomerDashboardActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        checkUser();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id;
        id = item.getItemId();
        if (id == R.id.logout){
            nAuth.signOut();
            checkUser();
        }
        return super.onOptionsItemSelected(item);
    }
}
