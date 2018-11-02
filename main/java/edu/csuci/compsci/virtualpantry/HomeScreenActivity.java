package edu.csuci.compsci.virtualpantry;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import database.PantryBaseHelper;

public class HomeScreenActivity extends AppCompatActivity {


    private Context mContext;
    private SQLiteDatabase mDatabase;

    private Button openPantry;
    private Button createPantry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = this.getApplicationContext();
        mDatabase = new PantryBaseHelper(mContext).getWritableDatabase();

        openPantry = (Button) findViewById(R.id.openpantry);
        openPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPantryScreen();

            }
        });

        createPantry = (Button) findViewById(R.id.create_pantry_button);
        createPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPantry();

            }
        });



    }

    public void openPantryScreen()
    {
        Intent intent = new Intent(this, PantryScreenActivity.class);
        startActivity(intent);
    }

    public void addPantry()
    {
        //TODO: Implement
    }

    //public #returnValue getPantry(UUID id){ return #returnValue;}
}
