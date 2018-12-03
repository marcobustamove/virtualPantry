package edu.csuci.compsci.virtualpantry;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import database.PantryBaseHelper;
import database.PantryDBSchema;
import database.PantryDBSchema.PantryTable;
import database.PantryDBSchema.ItemTable;

public class HomeScreenActivity extends AppCompatActivity
        implements CreatePantryFragment.CreatePantryListener, DeletePantryFragment.DeletePantryListener
{
    private static final String DIALOG_CREATE_PANTRY = "DialogCreatePantry";
    private static final String DIALOG_DELETE_PANTRY = "DialogDeletePantry";

    private Context mContext;
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    private ImageView listViewBars;
    private TextView pantryTitle;
    private Button mHeartIcon;
    private Button mDeletePantry;
    private TextView noPantryPrompt;
    private ImageView pantryImage;
    private Button mOpenPantry;
    private ImageView pantryBox;
    private Button mCreatePantry;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private String favoriteValue;
    private String currentPantryUUIDOnDisplay;

    private List<String> pantryUUIDS;

    //CursorAdapter for binding to a sql
    ListView listView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = this.getApplicationContext();
        writableDatabase = new PantryBaseHelper(mContext).getWritableDatabase();
        readableDatabase = new PantryBaseHelper(mContext).getReadableDatabase();


        pantryTitle = (TextView) findViewById(R.id.pantrytitle);
        noPantryPrompt = (TextView) findViewById(R.id.noPantryPrompt);
        pantryImage = (ImageView) findViewById(R.id.pantryImage);

        
        pantryBox = (ImageView) findViewById(R.id.pantrybox);
        pantryBox.setOnTouchListener(new OnSwipeTouchListener(mContext)
        {
            @Override
            public void onSwipeLeft()
            {
                Cursor cursor = getPantryDBWithFavoriteAsFirst();

                if(cursor.getCount() > 0)
                {
                    cursor.moveToNext();

                    while (!currentPantryUUIDOnDisplay.equals(cursor.getString(cursor.getColumnIndex(PantryTable.Cols.UUID))))
                    {
                        cursor.moveToNext();
                    }

                    if(cursor.moveToNext())
                    {

                        updatePantryBox(cursor);
                    }
                    else
                    {
                        cursor.moveToPosition(0);

                        updatePantryBox(cursor);

                    }
                }
            }

            @Override
            public void onSwipeRight()
            {
                Cursor cursor = getPantryDBWithFavoriteAsFirst();

                if(cursor.getCount() > 0)
                {
                    cursor.moveToNext();

                    while (!currentPantryUUIDOnDisplay.equals(cursor.getString(cursor.getColumnIndex(PantryTable.Cols.UUID))))
                    {
                        cursor.moveToNext();
                    }

                    if(cursor.moveToPrevious())
                    {
                        updatePantryBox(cursor);
                    }
                    else
                    {
                        cursor.moveToLast();
                        updatePantryBox(cursor);
                    }


                }
            }

        });

        mHeartIcon = (ToggleButton) findViewById(R.id.favicon);
        mHeartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(favoriteValue.equals("YES"))
                {
                    removeCurrentFavorite();
                    mHeartIcon.setBackgroundResource(R.drawable.defaultfavicon);
                }
                else
                {
                    setFavorite(pantryTitle.getText().toString());
                    mHeartIcon.setBackgroundResource(R.drawable.favicon);
                }
            }
        });

        mDeletePantry = (Button) findViewById(R.id.deletebutton);
        mDeletePantry.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                openDeletePantryDialog();
            }
        });

        mOpenPantry = (Button) findViewById(R.id.openpantry);
        mOpenPantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPantryScreen(currentPantryUUIDOnDisplay);

            }
        });

        listViewBars = (ImageView) findViewById(R.id.categorylist);
        listViewBars.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mDrawerLayout.openDrawer(GravityCompat.START);
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

        setPantryView();

        //This is for the drawer
        mDrawerLayout= (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        listView = (ListView) findViewById(R.id.simpleListView);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                String pantryUUID = pantryUUIDS.get(menuItem.getItemId());
                openPantryScreen(pantryUUID);

                return false;
            }
        });
        addMenuItemInNavMenuDrawer();

    }

    public void openPantryScreen(String pantryUUID)
    {
        Intent intent = new Intent(this, PantryScreenActivity.class);
        intent.putExtra("EXTRA_PANTRY_UUID", pantryUUID);
        startActivity(intent);
    }

    public void openCreatePantryDialog()
    {
        CreatePantryFragment fragment = new CreatePantryFragment();
        fragment.show(getSupportFragmentManager(), DIALOG_CREATE_PANTRY);
    }

    public void openDeletePantryDialog()
    {
        DeletePantryFragment fragment = new DeletePantryFragment();
        fragment.show(getSupportFragmentManager(), DIALOG_DELETE_PANTRY);
    }

    public boolean checkForDuplicatePantryNames(String inputPantryName)
    {
        boolean foundDuplicateName = false;

        Cursor cursor = readableDatabase.rawQuery("SELECT " + PantryTable.Cols.TITLE + " FROM " + PantryTable.NAME + " WHERE "
                + PantryTable.Cols.TITLE + " LIKE ?;", new String[] {inputPantryName});

        if(cursor.moveToNext() && cursor.getString(cursor.getColumnIndex(PantryTable.Cols.TITLE)).equals(inputPantryName))
            foundDuplicateName = true;

        cursor.close();


        return foundDuplicateName;
    }

    @Override
    public void createPantry(String inputPantryName)
    {
        if(checkForDuplicatePantryNames(inputPantryName))
        {
            Toast.makeText(getApplicationContext(), inputPantryName + " already exists!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ContentValues values = new ContentValues();
            values.put(PantryTable.Cols.UUID, UUID.randomUUID().toString());
            values.put(PantryTable.Cols.TITLE, inputPantryName);
            values.put(PantryTable.Cols.FAVORITE, "NO");

            writableDatabase.insert(PantryTable.NAME, null, values);
            addMenuItemInNavMenuDrawer();
            String[] projection = {PantryTable.Cols.UUID, PantryTable.Cols.TITLE, PantryTable.Cols.FAVORITE};
            String selection = PantryTable.Cols.TITLE + " = ?";
            String[] whereValue = {inputPantryName};

            Cursor cursor = readableDatabase.query(PantryTable.NAME, projection, selection, whereValue, null, null, null, null);

            cursor.moveToNext();
            updatePantryBox(cursor);
        }
    }

    @Override
    public void deletePantry()
    {
        String selectionForPantryTable = PantryTable.Cols.UUID + " LIKE ?";
        String selectionForItemTable = ItemTable.Cols.PANTRY_ID + " LIKE ?";
        String[] whereValue = { currentPantryUUIDOnDisplay };

        writableDatabase.delete(ItemTable.NAME, selectionForItemTable, whereValue);
        writableDatabase.delete(PantryTable.NAME, selectionForPantryTable, whereValue);
        addMenuItemInNavMenuDrawer();
        setPantryView();
    }

    public Cursor getPantryDBWithFavoriteAsFirst()
    {
        String[] projection = {PantryTable.Cols.UUID, PantryTable.Cols.TITLE, PantryTable.Cols.FAVORITE};
        String sortOrder = PantryTable.Cols.FAVORITE + " DESC";

        return readableDatabase.query(PantryTable.NAME, projection, null, null, null, null, sortOrder);
    }

    public void setPantryView()
    {
        int indexOfTitleColumn;
        int indexOfUUIDColumn;
        int indexOfFavoriteColumn;

        Cursor cursor = getPantryDBWithFavoriteAsFirst();

        if(cursor != null && cursor.getCount() > 0 && cursor.moveToNext())
        {
            indexOfTitleColumn = cursor.getColumnIndex(PantryTable.Cols.TITLE);
            indexOfUUIDColumn = cursor.getColumnIndex(PantryTable.Cols.UUID);
            indexOfFavoriteColumn = cursor.getColumnIndex(PantryTable.Cols.FAVORITE);

            favoriteValue = cursor.getString(indexOfFavoriteColumn);
            pantryTitle.setText(cursor.getString(indexOfTitleColumn));
            currentPantryUUIDOnDisplay = cursor.getString(indexOfUUIDColumn);
            noPantryPrompt.setText("");

            if(favoriteValue.equals("YES"))
            {
                mHeartIcon.setBackgroundResource(R.drawable.favicon);
            }
            else
            {
                mHeartIcon.setBackgroundResource(R.drawable.defaultfavicon);
            }

            makeIconsVisible();

        }
        else
        {
            hidePantryIcons();
        }

        cursor.close();
    }

    public void updatePantryBox(Cursor cursor)
    {
        int indexOfTitleColumn;
        int indexOfUUIDColumn;
        int indexOfFavoriteColumn;

        indexOfTitleColumn = cursor.getColumnIndex(PantryTable.Cols.TITLE);
        indexOfUUIDColumn = cursor.getColumnIndex(PantryTable.Cols.UUID);
        indexOfFavoriteColumn = cursor.getColumnIndex(PantryTable.Cols.FAVORITE);

        pantryTitle.setText(cursor.getString(indexOfTitleColumn));
        currentPantryUUIDOnDisplay = cursor.getString(indexOfUUIDColumn);
        favoriteValue = cursor.getString(indexOfFavoriteColumn);

        noPantryPrompt.setText("");

        if (favoriteValue.equals("YES"))
        {
            mHeartIcon.setBackgroundResource(R.drawable.favicon);
        } else
        {
            mHeartIcon.setBackgroundResource(R.drawable.defaultfavicon);
        }

        makeIconsVisible();
        addMenuItemInNavMenuDrawer();
    }

    public void makeIconsVisible()
    {
        mHeartIcon.setVisibility(View.VISIBLE);
        mDeletePantry.setVisibility(View.VISIBLE);
        mOpenPantry.setVisibility(View.VISIBLE);
        pantryImage.setVisibility(View.VISIBLE);
    }

    public void hidePantryIcons()
    {
        mHeartIcon.setVisibility(View.INVISIBLE);
        mDeletePantry.setVisibility(View.INVISIBLE);
        mOpenPantry.setVisibility(View.INVISIBLE);
        pantryImage.setVisibility(View.INVISIBLE);
        pantryTitle.setText("");
        noPantryPrompt.setText("You have no Pantries!\nStart by creating one!");
    }

    public void removeCurrentFavorite()
    {
        String whereClause = PantryTable.Cols.FAVORITE + " = ?";
        String[] whereValue = { "YES" };
        ContentValues cv = new ContentValues();
        cv.put(PantryTable.Cols.FAVORITE, "NO");
        writableDatabase.update(PantryTable.NAME, cv, whereClause, whereValue);
        favoriteValue = "NO";
    }

    public void setFavorite(String pantryName)
    {
        removeCurrentFavorite();

        String whereClause = PantryTable.Cols.TITLE + " = ?";
        String[] whereValue = { pantryName };
        ContentValues cv = new ContentValues();
        cv.put(PantryTable.Cols.FAVORITE, "YES");
        writableDatabase.update(PantryTable.NAME, cv, whereClause, whereValue);
        favoriteValue = "YES";
        addMenuItemInNavMenuDrawer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addMenuItemInNavMenuDrawer()
    {
        int indexOfTitleColumn;
        int indexOfUUIDColumn;

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();

        Cursor cursor = getPantryDBWithFavoriteAsFirst();
        indexOfTitleColumn = cursor.getColumnIndex(PantryTable.Cols.TITLE);
        indexOfUUIDColumn = cursor.getColumnIndex(PantryTable.Cols.UUID);

        pantryUUIDS = new ArrayList<>();
        menu.clear();
        while(cursor.moveToNext())
        {
            String pantryName = cursor.getString(indexOfTitleColumn);
            String pantryUUID = cursor.getString(indexOfUUIDColumn);

            pantryUUIDS.add(pantryUUID);
            menu.add(pantryName);
        }
        cursor.close();
    }
}