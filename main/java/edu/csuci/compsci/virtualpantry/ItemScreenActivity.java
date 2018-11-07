package edu.csuci.compsci.virtualpantry;

import android.content.ContentValues;
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


import java.util.UUID;

import database.PantryBaseHelper;
import database.PantryDBSchema.ItemTable;

public class ItemScreenActivity extends AppCompatActivity  implements MyRecyclerViewAdapter.ItemClickListener, AddItemFragment.AddItemListener {

    MyRecyclerViewAdapter adapter;

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static final String DIALOG_ADD_ITEM = "DialogAddItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        mContext = this.getApplicationContext();
        mDatabase = new PantryBaseHelper(mContext).getWritableDatabase();

        String[] data = {"Apples", "Bananas", "Oranges", "Watermelon", "Peaches", "Kiwi", "Strawberries", "Grapes", "Avocado", "Pineapple", "Kiwano Melon", "Dragonfruit", "Tomato"};
        /* test array for displaying 150 grid items
        String data[] = new String[150];
        for(int i = 0; i < data.length; i++)
        {
            data[i] = Integer.toString(i+1);
        }
        */

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
        values.put(ItemTable.Cols.DATE, expirationMonth + "/" + expirationDay + "/" + expirationYear);

        mDatabase.insert(ItemTable.NAME, null, values);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
    }

}
