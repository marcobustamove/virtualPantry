package edu.csuci.compsci.virtualpantry;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    public static final String USERNAME_FIELD = "username";
    public static final String EMAIL_FIELD = "email";
    public static final String PASSWORD_FIELD = "password";
    public static final String USERS_COLLECTION = "users";
    private static final int MINIMUM_PASSWORD_LENGTH = 6;
    public static final String TAG = "FireBaseMsg";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button createAccountSubmit;
    private Button login;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText reenterPassword;

    private SharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        mySharedPreferences = getSharedPreferences("LoggedIn", Activity.MODE_PRIVATE);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        reenterPassword = (EditText) findViewById(R.id.reenter_password);


        createAccountSubmit = (Button) findViewById(R.id.create_account);
        createAccountSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUsername = username.getText().toString();
                String inputEmail = email.getText().toString();
                String inputPass = password.getText().toString();
                String inputRePass = reenterPassword.getText().toString();

                if(inputUsername.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Blank Username", Toast.LENGTH_SHORT).show();
                }
                else if(inputEmail.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Blank Email", Toast.LENGTH_SHORT).show();
                }
                else if(inputPass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Blank Password!", Toast.LENGTH_SHORT).show();
                }
                else if(inputRePass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Confirm Password", Toast.LENGTH_SHORT).show();
                }
                else if(inputPass.length() < MINIMUM_PASSWORD_LENGTH)
                {
                    Toast.makeText(getApplicationContext(),"Password must be 6 characters long", Toast.LENGTH_SHORT).show();
                }
                else if(!inputPass.equals(inputRePass))
                {
                    Toast.makeText(getApplicationContext(),"Passwords Don't Match", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    createNewUser(inputUsername.toLowerCase(), inputEmail, inputPass);
                }
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void createNewUser(final String inputUsername, final String inputEmail, final String inputPass)
    {
        final String sha256hex = Hashing.sha256().hashString(inputPass, StandardCharsets.UTF_8).toString();
        final DocumentReference docRef = db.document(USERS_COLLECTION + "/" + inputUsername);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    Toast.makeText(getApplicationContext(),"Username Unavailable", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Map<String, Object> userToAdd = new HashMap<>();
                    userToAdd.put(USERNAME_FIELD, inputUsername);
                    userToAdd.put(EMAIL_FIELD, inputEmail);
                    userToAdd.put(PASSWORD_FIELD, sha256hex);

                    docRef.set(userToAdd)
                            .addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    Log.d(TAG, "Document has been saved!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                    setLoginStatus();
                    openHomeScreen();
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

