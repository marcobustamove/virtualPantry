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
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import database.PantryBaseHelper;
import database.PantryDBSchema.PantryTable;

public class HomeScreenActivity extends AppCompatActivity  implements CreatePantryFragment.CreatePantryListener
{


    private Context mContext;
    private SQLiteDatabase mWritableDatabase;
    private static final String DIALOG_CREATE_PANTRY = "DialogCreatePantry";
    private Button mOpenPantry;
    private Button mCreatePantry;
    private Button mHeartButton;
    private TextView pantryTitle;
    private String userInputPantryName;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ArrayAdapter<String> mAdapter;
    private ListView mDrawerList;
    private String favoriteValue;
    private int indexOfFavoriteColumn;
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
        mReadableDatabase = new PantryBaseHelper(mContext).getReadableDatabase();

        //TODO - Marco's In Progress Work
        //This section will be for the filling of the first pantry to show to the user
        //In other words, if there is a favorite pantry, then grab the entry from the database
        //and use the info to fill in the red heart and the title
        //Otherwise if there is no favorite, grab the first pantry in database and fill in info
        //This will ultimately be where the scroll view where go

        mHeartButton = (ToggleButton) findViewById(R.id.favicon);
        mHeartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(favoriteValue.equals("YES"))
                {
                    removeCurrentFavorite();
                    mHeartButton.setBackgroundResource(R.drawable.defaultfavicon);
                }
                else
                {
                    setFavorite(pantryTitle.getText().toString());
                    mHeartButton.setBackgroundResource(R.drawable.favicon);
                }
            }
        });

        pantryTitle = (TextView) findViewById(R.id.pantrytitle);


        setPantryView();


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


        //This is for the drawer
        mDrawerLayout= (DrawerLayout)findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        listView = (ListView) findViewById(R.id.simpleListView);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case 0:
                        openPantryScreen();
                }
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
        values.put(PantryTable.Cols.FAVORITE, "NO");

        mWritableDatabase.insert(PantryTable.NAME, null, values);
        addMenuItemInNavMenuDrawer();
    }

    public void setPantryView()
    {
        int indexOfTitleColumn;

        String[] projection = {PantryTable.Cols.UUID, PantryTable.Cols.TITLE, PantryTable.Cols.FAVORITE};
        String selection = PantryTable.Cols.FAVORITE + " = ?";
        String[] whereValue = {"YES"};

        Cursor cursor = mReadableDatabase.query(PantryTable.NAME, projection, selection, whereValue,null,null,null);

        if(cursor != null && cursor.getCount() > 0 && cursor.moveToNext())
        {
            indexOfTitleColumn = cursor.getColumnIndex(PantryTable.Cols.TITLE);
            pantryTitle.setText(cursor.getString(indexOfTitleColumn));

            indexOfFavoriteColumn = cursor.getColumnIndex(PantryTable.Cols.FAVORITE);
            favoriteValue = cursor.getString(indexOfFavoriteColumn);

            mHeartButton.setBackgroundResource(R.drawable.favicon);


        }
        else
        {
            cursor = mReadableDatabase.query(PantryTable.NAME, projection, null, null,null,null,null);

            if(cursor != null && cursor.getCount() > 0 && cursor.moveToNext())
            {
                indexOfTitleColumn = cursor.getColumnIndex(PantryTable.Cols.TITLE);
                pantryTitle.setText(cursor.getString(indexOfTitleColumn));

                indexOfFavoriteColumn = cursor.getColumnIndex(PantryTable.Cols.FAVORITE);
                favoriteValue = cursor.getString(indexOfFavoriteColumn);
                mHeartButton.setBackgroundResource(R.drawable.defaultfavicon);
            }
        }

        cursor.close();
    }

    public void removeCurrentFavorite()
    {
        String whereClause = PantryTable.Cols.FAVORITE + " = ?";
        String[] whereValue = { "YES" };
        ContentValues cv = new ContentValues();
        cv.put(PantryTable.Cols.FAVORITE, "NO");
        mWritableDatabase.update(PantryTable.NAME, cv, whereClause, whereValue);
        favoriteValue = "NO";
    }
    public void setFavorite(String pantryName)
    {
        removeCurrentFavorite();

        String whereClause = PantryTable.Cols.TITLE + " = ?";
        String[] whereValue = { pantryName };
        ContentValues cv = new ContentValues();
        cv.put(PantryTable.Cols.FAVORITE, "YES");
        mWritableDatabase.update(PantryTable.NAME, cv, whereClause, whereValue);
        favoriteValue = "YES";

    }

    private void addMenuItemInNavMenuDrawer()
    {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        mReadableDatabase = new PantryBaseHelper(mContext).getReadableDatabase();
        String[]position = {PantryTable.Cols.TITLE, PantryTable.Cols.UUID};

        Cursor cursor = mReadableDatabase.query(PantryTable.NAME,position,null,null,null,null,null);


        List<String> itemIds = new ArrayList<>();
        menu.clear();
        while(cursor.moveToNext()) {
            String pantryName = cursor.getString(0);
            String pantryUUID = cursor.getString(1);
            //itemIds.add(itemId)
            // System.out.println(itemId);
            menu.add(pantryName);
        }
        cursor.close();
        //navView.invalidate();

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