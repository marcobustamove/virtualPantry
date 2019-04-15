package edu.csuci.compsci.virtualpantry;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.StandardCharsets;

public class LoginScreenActivity extends AppCompatActivity {

    public static final String PASSWORD_FIELD = "password";
    public static final String USERS_COLLECTION = "users";

    private Button createAccountButton;
    private Button forgotPasswordButton;
    private Button loginButton;
    private EditText username;
    private EditText password;

    private SharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mySharedPreferences = getSharedPreferences("LoggedIn", Activity.MODE_PRIVATE);

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
                String inputUsername = username.getText().toString();
                String inputPass = password.getText().toString();

                if(inputUsername.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter username", Toast.LENGTH_SHORT).show();
                }
                else if(inputPass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    attemptLogin(inputUsername, inputPass);
                }
            }

        });

        username = (EditText) findViewById(R.id.username);
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

    private void attemptLogin(String inputUsername, final String inputPass)
    {
        DocumentReference docRef = FirebaseFirestore.getInstance().document(USERS_COLLECTION + "/" + inputUsername.toLowerCase());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    String hashedPass = documentSnapshot.getString(PASSWORD_FIELD);
                    String hashedInputPass = Hashing.sha256().hashString(inputPass, StandardCharsets.UTF_8).toString();

                    if (hashedPass.equals(hashedInputPass))
                    {
                        setLoginStatus();
                        openHomeScreen();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Incorrect username or password", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Incorrect username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setLoginStatus()
    {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("loggedIn", true);
        editor.apply();
    }

}
