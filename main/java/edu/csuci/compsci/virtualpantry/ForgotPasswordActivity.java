package edu.csuci.compsci.virtualpantry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity  {


    private Button forgotPasswordSubmitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_screen);

        forgotPasswordSubmitButton = (Button) findViewById(R.id.forgot_Password_submit);
        forgotPasswordSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Email Sent", Toast.LENGTH_SHORT).show();
                openLoginScreenActivity();

            }
        });

    }
    public void openLoginScreenActivity()
    {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }


}
