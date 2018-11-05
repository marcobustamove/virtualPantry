package edu.csuci.compsci.virtualpantry;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import database.PantryBaseHelper;
import database.PantryDBSchema.PantryTable;

public class HomeScreenActivity extends AppCompatActivity {


    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static final String DIALOG_CREATE_PANTRY = "DialogCreatePantry";
    private Button mOpenPantry;
    private Button mCreatePantry;

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
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                CreatePantryFragment dialog = new CreatePantryFragment();
                dialog.show(manager, DIALOG_CREATE_PANTRY);

            }
        });



    }

    public void openPantryScreen()
    {
        Intent intent = new Intent(this, PantryScreenActivity.class);
        startActivity(intent);
    }

    private static ContentValues getContentValues(String pantryName) {
        ContentValues values = new ContentValues();

        values.put(PantryTable.Cols.TITLE, pantryName);

        return values;
    }

    public void createPantry(String pantryName)
    {
        ContentValues values = getContentValues(pantryName);
        mDatabase.insert(PantryTable.Cols.TITLE, null, values);
    }

}
