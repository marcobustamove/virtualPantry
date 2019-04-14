package edu.csuci.compsci.virtualpantry;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginScreenActivity extends AppCompatActivity {

    private Button createAccountButton;
    private Button forgotPasswordButton;
    private Button loginButton;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final SharedPreferences mySharedPreferences = getSharedPreferences("LoggedIn", Activity.MODE_PRIVATE);

        createAccountButton = (Button) findViewById(R.id.Create_Account);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAccount();
            }

        });

        forgotPasswordButton = (Button) findViewById(R.id.ForgotPassword);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgotPassword();
            }

        });

        loginButton = (Button) findViewById(R.id.login_submit);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do some username and password checking
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putBoolean("loggedIn", true);
                editor.apply();
                openHomeScreen();
                //Need to add to HomeScreenActivity a logout button
                //When logout button is called we will get the
                //SharedPreferences and set loggedIn to false
            }

        });

        username = (EditText) findViewById(R.id.email);
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                username.setCursorVisible(true);
            }
        });

        password = (EditText) findViewById(R.id.password);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                password.setCursorVisible(true);
            }
        });

    }

    public void openHomeScreen()
    {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void openCreateAccount()
    {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }
    public void openForgotPassword()
    {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

}
