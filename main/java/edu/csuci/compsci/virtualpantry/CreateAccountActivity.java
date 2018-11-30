package edu.csuci.compsci.virtualpantry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateAccountActivity extends AppCompatActivity {

    private Button createAccountSubmit;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        createAccountSubmit = (Button) findViewById(R.id.create_account_submit_button);
        createAccountSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomeScreen();
            }

        });

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }

    public void openHomeScreen()
    {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
    }

}

