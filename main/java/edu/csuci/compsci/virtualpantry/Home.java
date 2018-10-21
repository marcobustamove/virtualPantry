package edu.csuci.compsci.virtualpantry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity  {

    MyRecyclerViewAdapter adapter;
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
                openPantryScreen();

            }
        });


        /**
        // data to populate the RecyclerView with
        String[] data = {"Apples", "Bananas", "Oranges", "Watermelon", "Peaches", "Kiwi", "Strawberries", "Grapes", "Avocado", "Pineapple", "Kiwano Melon", "Dragonfruit", "Tomato"};
        /* test array for displaying 150 grid items
        String data[] = new String[150];
        for(int i = 0; i < data.length; i++)
        {
            data[i] = Integer.toString(i+1);
        }
        //put back slash astrix
        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvNumbers);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, data);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
    }
    */
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
    public void openPantryScreen()
    {
        Intent intent = new Intent(this, pantryScreenActivity.class);
        startActivity(intent);
    }


}

