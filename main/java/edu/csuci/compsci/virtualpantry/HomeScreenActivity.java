package edu.csuci.compsci.virtualpantry;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.UUID;

import database.PantryBaseHelper;
import database.PantryDBSchema.PantryTable;

public class HomeScreenActivity extends AppCompatActivity implements CreatePantryFragment.CreatePantryListener
{


    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static final String DIALOG_CREATE_PANTRY = "DialogCreatePantry";
    private Button mOpenPantry;
    private Button mCreatePantry;
    private String userInputPantryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = this.getApplicationContext();
        mDatabase = new PantryBaseHelper(mContext).getWritableDatabase();

        mOpenPantry = (Button) findViewById(R.id.openpantry);
        mOpenPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPantryScreen();

            }
        });


        mCreatePantry = (Button) findViewById(R.id.create_pantry_button);
        mCreatePantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openCreatePantryDialog();
                createPantry(userInputPantryName);
            }
        });



    }

    public void openPantryScreen()
    {
        Intent intent = new Intent(this, PantryScreenActivity.class);
        startActivity(intent);
    }

    public void openCreatePantryDialog()
    {
        CreatePantryFragment fragment = new CreatePantryFragment();
        fragment.show(getSupportFragmentManager(), DIALOG_CREATE_PANTRY);
    }

    @Override
    public void applyTexts(String inputPantryName)
    {
        userInputPantryName = inputPantryName;
    }


    public void createPantry(String pantryName)
    {
        ContentValues values = new ContentValues();
        values.put(PantryTable.Cols.UUID, UUID.randomUUID().toString());
        values.put(PantryTable.Cols.TITLE, pantryName);

        mDatabase.insert(PantryTable.NAME, null, values);
    }

    public void updatePantry(String newPantryName, UUID pantryID)
    {
        ContentValues values = new ContentValues();

        values.put(PantryTable.Cols.TITLE, newPantryName);

        mDatabase.update(PantryTable.NAME, values,
                PantryTable.Cols.UUID + " = ?",
                new String[] { pantryID.toString() });
    }

}
