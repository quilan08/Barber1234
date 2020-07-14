package com.example.barberchair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.FragmentNavigator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.Dash;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Action bar title
        actionBar = getSupportActionBar();
        actionBar.setTitle("The Barber");


        firebaseAuth = FirebaseAuth.getInstance();

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        //As a default selections
        //home Fragment
        actionBar.setTitle("Home");
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentz,fragment,"");
        fragmentTransaction.commit();

    }
    private  BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        // here handle clicks, this where color changes
                        case R.id.nav_home:
                            //home Fragment
                            actionBar.setTitle("Home");
                            HomeFragment fragment1 = new HomeFragment();
                            FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction1.replace(R.id.contentz,fragment1,"");
                            fragmentTransaction1.commit();
                            return true;
                        case R.id.nav_profile:
                            //profile Fragment
                            actionBar.setTitle("Profile");
                            ProfileFragment fragment2 = new ProfileFragment();
                            FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction2.replace(R.id.contentz,fragment2,"");
                            fragmentTransaction2.commit();
                            return true;
                        case R.id.nav_users:
                            //user fragment
                            actionBar.setTitle("Users");
                            UsersFragment fragment3 = new UsersFragment();
                            FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction3.replace(R.id.contentz,fragment3,"");
                            fragmentTransaction3.commit();
                            return true;
                    }

                    return false;
                }
            };
    private void  checkUserstaus(){
        // get the current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null ){
            //then signed in stay here
        }
        else
        {
            Intent  intent  = new Intent(DashboardActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        checkUserstaus();
        super.onStart();
    }
    // inflate option menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    /* handle the menu item clicks */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get id
        int id;

        id = item.getItemId();
        if (id == R.id.logout){
            firebaseAuth.signOut();
            checkUserstaus();
        }
        return super.onOptionsItemSelected(item);
    }
}
