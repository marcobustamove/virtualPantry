package edu.csuci.compsci.virtualpantry;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import database.PantryBaseHelper;
import database.PantryDBSchema.ItemTable;

public class ItemScreenActivity extends AppCompatActivity  implements MyRecyclerViewAdapter.ItemClickListener, AddItemFragment.AddItemListener {

    MyRecyclerViewAdapter adapter;

    private Context mContext;
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;
    private RecyclerView ItemsRecyclerView;
    private TextView categoryTitle;
    private String pantryUUID;
    private String pantryCategory;
    private ArrayList<String> itemList;

    private String currentSortingOrder;
    private Button sortingMethod;
    private Button mAddItemButton;

    private static final int FULL = 1;
    private static final int LOW = 2;
    private static final int EMPTY = 3;
    private static final int EXPIRED = 4;

    private static final String DIALOG_ADD_ITEM = "DialogAddItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        this.pantryUUID = this.getIntent().getStringExtra("EXTRA_PANTRY_UUID");
        this.pantryCategory = this.getIntent().getStringExtra("EXTRA_PANTRY_CATEGORY");

        categoryTitle = (TextView) findViewById(R.id.category);
        categoryTitle.setText(pantryCategory);

        mContext = this.getApplicationContext();
        writableDatabase = new PantryBaseHelper(mContext).getWritableDatabase();
        readableDatabase = new PantryBaseHelper(mContext).getReadableDatabase();

        sortingMethod = (Button) findViewById(R.id.sort);
        currentSortingOrder = "A-Z";
        sortingMethod.setText("A-Z");
        sortingMethod.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                switch(currentSortingOrder)
                {
                    case "A-Z":
                        currentSortingOrder = "STATUS";
                        sortingMethod.setText(getResources().getString(R.string.status));
                        itemList = sortItemsByStatus();
                        setUpRecyclerView();
                        break;
                    case "STATUS":
                        currentSortingOrder = "EXP";
                        sortingMethod.setText(getResources().getString(R.string.expiration));
                        itemList = sortItemsByExpDate();
                        setUpRecyclerView();
                        break;
                    case "EXP":
                        currentSortingOrder = "A-Z";
                        sortingMethod.setText(getResources().getString(R.string.alphabetically));
                        itemList = sortItemsAlphabetically();
                        setUpRecyclerView();
                        break;
                }
            }
        });

        initializeArrayList();

        ItemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        setUpRecyclerView();

        mAddItemButton = (Button) findViewById(R.id.addItemButton);
        mAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                AddItemFragment dialog = new AddItemFragment();
                dialog.show(manager,DIALOG_ADD_ITEM);
            }
        });
    }

    public void setUpRecyclerView()
    {
        int numberOfColumns = 3;
        ItemsRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, itemList);
        adapter.setClickListener(this);
        ItemsRecyclerView.setAdapter(adapter);
    }

    public void initializeArrayList()
    {
        switch(currentSortingOrder)
        {
            case "A-Z":
                itemList = sortItemsAlphabetically();
                break;

            case "STATUS":
                itemList = sortItemsByStatus();
                break;

            case "EXP":
                itemList = sortItemsByExpDate();
                break;
        }
    }

    public Cursor getEntireItemDatabase()
    {
        String[] projection = {ItemTable.Cols.NAME, ItemTable.Cols.UUID, ItemTable.Cols.STATUS, ItemTable.Cols.PANTRY_ID, ItemTable.Cols.DATE};
        String selection = ItemTable.Cols.PANTRY_ID + "=?" + " AND " + ItemTable.Cols.CATEGORY + "=?";
        String[] selectVals = {pantryUUID, pantryCategory};

        return readableDatabase.query(ItemTable.NAME, projection, selection, selectVals, null, null, null);
    }

    public Cursor getItemDBSortedByStatus()
    {
        String[] projection = {ItemTable.Cols.NAME, ItemTable.Cols.STATUS};
        String selection = ItemTable.Cols.PANTRY_ID + "=?" + " AND " + ItemTable.Cols.CATEGORY + "=?";
        String[] selectValues = {pantryUUID, pantryCategory};
        String sortOrder = ItemTable.Cols.STATUS + " ASC";

        return readableDatabase.query(ItemTable.NAME, projection, selection, selectValues, null, null, sortOrder);

    }

    public Cursor getItemDBSortedByExpDate()
    {
        String[] projection = {ItemTable.Cols.NAME, ItemTable.Cols.STATUS};
        String selection = ItemTable.Cols.PANTRY_ID + "=?" + " AND " + ItemTable.Cols.CATEGORY + "=?";
        String[] selectValues = {pantryUUID, pantryCategory};
        String sortOrder = ItemTable.Cols.DATE + " ASC";

        return readableDatabase.query(ItemTable.NAME, projection, selection, selectValues, null, null, sortOrder);
    }

    public ArrayList<String> sortItemsAlphabetically()
    {
        ArrayList<String> temp = new ArrayList<>();
        Cursor cursor = getEntireItemDatabase();

        while(cursor.moveToNext())
        {
            temp.add(cursor.getString(cursor.getColumnIndex(ItemTable.Cols.NAME)));
        }
        cursor.close();

        Collections.sort(temp, String.CASE_INSENSITIVE_ORDER);

        return temp;
    }

    public ArrayList<String> sortItemsByExpDate()
    {
        ArrayList<String> temp = new ArrayList<>();
        Cursor cursor = getItemDBSortedByExpDate();

        while(cursor.moveToNext())
        {
            temp.add(cursor.getString(cursor.getColumnIndex(ItemTable.Cols.NAME)));
        }

        cursor.close();

        return temp;
    }

    public ArrayList<String> sortItemsByStatus()
    {
        ArrayList<String> temp = new ArrayList<>();

        Cursor cursor = getItemDBSortedByStatus();

        while(cursor.moveToNext())
        {
            temp.add(cursor.getString(cursor.getColumnIndex(ItemTable.Cols.NAME)));
        }

        cursor.close();

        return temp;
    }

    @Override
    public void AddItem(String newItemName, Boolean expirable, int expirationMonth, int expirationDay, int expirationYear)
    {
        ContentValues values = new ContentValues();
        values.put(ItemTable.Cols.UUID, UUID.randomUUID().toString());
        values.put(ItemTable.Cols.NAME, newItemName);
        values.put(ItemTable.Cols.DATE, expirationYear + "/" + (expirationMonth+1) + "/" + expirationDay);
        values.put(ItemTable.Cols.PANTRY_ID, this.pantryUUID);
        values.put(ItemTable.Cols.CATEGORY, this.pantryCategory);
        values.put(ItemTable.Cols.STATUS, FULL);

        writableDatabase.insert(ItemTable.NAME, null, values);
        initializeArrayList();
        setUpRecyclerView();
    }

    @Override
    public void onItemClick(View view, int position)
    {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
    }
    @Override
    public void itemDeleteInfo(View view, int position)
    {
        String selectionForItemTable = ItemTable.Cols.PANTRY_ID + " LIKE ?" + " AND " + ItemTable.Cols.CATEGORY + " LIKE ?" + " AND " + ItemTable.Cols.NAME + " LIKE ?";
        String[] whereValue = { this.pantryUUID, this.pantryCategory, itemList.get(position)};

        writableDatabase.delete(ItemTable.NAME, selectionForItemTable, whereValue);
        initializeArrayList();
        setUpRecyclerView();

    }
}
