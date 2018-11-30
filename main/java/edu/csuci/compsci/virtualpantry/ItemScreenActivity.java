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


import java.util.ArrayList;
import java.util.UUID;

import database.PantryBaseHelper;
import database.PantryDBSchema.ItemTable;

public class ItemScreenActivity extends AppCompatActivity  implements MyRecyclerViewAdapter.ItemClickListener, AddItemFragment.AddItemListener {

    MyRecyclerViewAdapter adapter;

    private Context mContext;
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;
    private String pantryUUID;
    private String pantryCategory;
    private ArrayList<String> data;

    private static final String DIALOG_ADD_ITEM = "DialogAddItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        this.pantryUUID = this.getIntent().getStringExtra("EXTRA_PANTRY_UUID");
        this.pantryCategory = this.getIntent().getStringExtra("EXTRA_PANTRY_CATEGORY");

        mContext = this.getApplicationContext();
        writableDatabase = new PantryBaseHelper(mContext).getWritableDatabase();
        readableDatabase = new PantryBaseHelper(mContext).getReadableDatabase();

        data = new ArrayList<>();
        String[] projection = {ItemTable.Cols.NAME, ItemTable.Cols.UUID};
        String selection = ItemTable.Cols.PANTRY_ID + "=?" + " AND " + ItemTable.Cols.CATEGORY + "=?";
        String[] selectVals = {pantryUUID, pantryCategory};

        Cursor cursor = readableDatabase.query(ItemTable.NAME, projection, selection, selectVals, null, null, null);


        while(cursor.moveToNext())
        {
            data.add(cursor.getString(cursor.getColumnIndex(ItemTable.Cols.NAME)));
        }

        cursor.close();

        // set up the RecyclerView
        RecyclerView ItemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        int numberOfColumns = 3;
        ItemsRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, data);
        adapter.setClickListener(this);
        ItemsRecyclerView.setAdapter(adapter);


        //Add item button
        Button mAddItemButton = (Button) findViewById(R.id.addItemButton);
        mAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                AddItemFragment dialog = new AddItemFragment();
                dialog.show(manager,DIALOG_ADD_ITEM);
            }
        });

    }

    @Override
    public void AddItem(String newItemName, Boolean expirable, int expirationMonth, int expirationDay, int expirationYear)
    {
        ContentValues values = new ContentValues();
        values.put(ItemTable.Cols.UUID, UUID.randomUUID().toString());
        values.put(ItemTable.Cols.NAME, newItemName);
        values.put(ItemTable.Cols.DATE, (expirationMonth+1) + "/" + expirationDay + "/" + expirationYear);
        values.put(ItemTable.Cols.PANTRY_ID, this.pantryUUID);
        values.put(ItemTable.Cols.CATEGORY, this.pantryCategory);

        writableDatabase.insert(ItemTable.NAME, null, values);
        data.add(newItemName);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
    }

}
