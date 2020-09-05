package com.hypoalien.bunkmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import org.json.JSONObject;
import static com.hypoalien.bunkmate.LoginActivity.MY_PREFS_NAME;

public class MainActivity extends AppCompatActivity {
    Boolean aBoolean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        aBoolean = prefs.getBoolean("loginStatus",false);
        if(aBoolean){
            final Handler handler = new Handler();
            //Do something after 100ms
            handler.postDelayed(this::openHomeActivity, 100);
        }else {
            final Handler handler = new Handler();
            //Do something after 100ms
            handler.postDelayed(this::openLoginActivity, 100);
        }

    }
    public void openLoginActivity(){

        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void openHomeActivity(){

        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }


}

