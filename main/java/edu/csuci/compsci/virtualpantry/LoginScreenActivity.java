package edu.csuci.compsci.virtualpantry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginScreenActivity extends AppCompatActivity {

    private Button createAccountButton;
    private Button forgotPasswordButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

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
                openHomeScreen();
            }

        });
    }

    public void openHomeScreen()
    {
        Intent intent = new Intent(this, HomeScreenActivity.class);
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
