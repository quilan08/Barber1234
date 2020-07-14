package com.example.barberchair;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by goutam on 29/2/16.
 */
public class MainActivity extends AppCompatActivity {
    public Button barbbtn,customerbtn;

private final int SPLASH_DISPLAY_LENGTH = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // to include or exclude some (toolbar, actionbar and so on).
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        ActionBar actionBar = getSupportActionBar();

        barbbtn = findViewById(R.id.thebarberbtn);
        customerbtn = findViewById(R.id.thecustomerbtn);

        barbbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterbarberMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        customerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegistercustomerMainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}