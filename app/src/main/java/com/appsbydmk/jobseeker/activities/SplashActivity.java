package com.appsbydmk.jobseeker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        this.startActivity(mainIntent);
        this.finish();
    }
}
