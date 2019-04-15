package edu.csuci.compsci.virtualpantry;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;

        final SharedPreferences mySharedPreferences = getSharedPreferences("LoggedIn", Activity.MODE_PRIVATE);

        if(mySharedPreferences.getBoolean("loggedIn", false))
        {
            intent = new Intent(this, HomeScreenActivity.class);
        }
        else
        {
            intent = new Intent(this, LoginScreenActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
