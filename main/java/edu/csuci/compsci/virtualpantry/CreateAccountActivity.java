package edu.csuci.compsci.virtualpantry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateAccountActivity extends AppCompatActivity {

    private Button createAccountSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        createAccountSubmitButton = (Button) findViewById(R.id.create_account_submit_button);
        createAccountSubmitButton.setOnClickListener(new View.OnClickListener() {
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

}

