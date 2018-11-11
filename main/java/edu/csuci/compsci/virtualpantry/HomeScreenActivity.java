package edu.csuci.compsci.virtualpantry;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import database.PantryBaseHelper;
import database.PantryDBSchema.PantryTable;

public class HomeScreenActivity extends AppCompatActivity implements CreatePantryFragment.CreatePantryListener
{


    private Context mContext;
    private SQLiteDatabase mWritableDatabase;
    private static final String DIALOG_CREATE_PANTRY = "DialogCreatePantry";
    private Button mOpenPantry;
    private Button mCreatePantry;
    private String userInputPantryName;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ArrayAdapter<String> mAdapter;
    private ListView mDrawerList;
    //CursorAdapter for binding to a sql
    ListView listView;
    private SQLiteDatabase mReadableDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = this.getApplicationContext();
        mWritableDatabase = new PantryBaseHelper(mContext).getWritableDatabase();

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
            }
        });






        mDrawerLayout= (DrawerLayout)findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        listView = (ListView) findViewById(R.id.simpleListView);
        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        mReadableDatabase = new PantryBaseHelper(mContext).getReadableDatabase();

        String[] position = {"TITLE"};

        Cursor cursor = mReadableDatabase.query(PantryTable.NAME,position,null,null,null,null,null);

        ArrayList itemIds = new ArrayList();
        while(cursor.moveToNext()) {
            String itemId = cursor.getString(0);
            itemIds.add(itemId);
            System.out.println(itemId);
            menu.add(itemId);
        }
        cursor.close();*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
        addMenuItemInNavMenuDrawer();




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
    public void createPantry(String inputPantryName)
    {
        userInputPantryName = inputPantryName;
        ContentValues values = new ContentValues();
        values.put(PantryTable.Cols.UUID, UUID.randomUUID().toString());
        values.put(PantryTable.Cols.TITLE, userInputPantryName);

        mWritableDatabase.insert(PantryTable.NAME, null, values);
        addMenuItemInNavMenuDrawer();
    }

    public void updatePantry(String newPantryName, UUID pantryID)
    {
        ContentValues values = new ContentValues();

        values.put(PantryTable.Cols.TITLE, newPantryName);

        mWritableDatabase.update(PantryTable.NAME, values,
                PantryTable.Cols.UUID + " = ?",
                new String[] { pantryID.toString() });
    }

    private void addMenuItemInNavMenuDrawer()
    {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        mReadableDatabase = new PantryBaseHelper(mContext).getReadableDatabase();
        String[]position = {PantryTable.Cols.TITLE};

        Cursor cursor = mReadableDatabase.query(PantryTable.NAME,position,null,null,null,null,null);


        List<String> itemIds = new ArrayList<>();
        menu.clear();
        while(cursor.moveToNext()) {
            String itemId = cursor.getString(0);
            //itemIds.add(itemId);
            //System.out.println(itemId);
            menu.add(itemId);

        }
        cursor.close();

        /*
        Menu menu = navView.getMenu();
        //Menu submenu = menu.addSubMenu("New Super SubMenu");
        menu.add("Super Item1");
        menu.add("Super Item2");
        menu.add("Super Item3");
        navView.invalidate();*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
