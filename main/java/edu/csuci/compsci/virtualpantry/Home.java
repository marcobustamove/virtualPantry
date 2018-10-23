package edu.csuci.compsci.virtualpantry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity  {

    private Button createAccountButton;
    private Button ForgotPasswordButton;
    private Button loginSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        createAccountButton = (Button) findViewById(R.id.Create_Account);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();

            }
        });
        ForgotPasswordButton = (Button) findViewById(R.id.ForgotPassword);
        ForgotPasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openForgotPasswordActivity();
            }
        });

        loginSubmitButton = (Button) findViewById(R.id.login_submit);
        loginSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openHomeScreen();

            }
        });

    }
    public void openLoginActivity()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void openForgotPasswordActivity()
    {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
    public void openHomeScreen()
    {
        Intent intent = new Intent(this, homeactivity.class);
        startActivity(intent);
    }


}

