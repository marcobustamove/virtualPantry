package edu.csuci.compsci.virtualpantry;

import android.content.ClipData;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import database.PantryBaseHelper;
import database.PantryDBSchema;
import database.PantryDBSchema.ItemTable;

public class ItemScreenActivity extends AppCompatActivity  implements MyRecyclerViewAdapter.ItemClickListener, AddItemFragment.AddItemListener {

    MyRecyclerViewAdapter adapter;

    private Context mContext;
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    private LinearLayout detailsLayout;
    private TextView itemNameTextView;
    private TextView itemExpirationTextView;
    private Button statusSetter1;
    private Button statusSetter2;

    private RecyclerView ItemsRecyclerView;
    private TextView categoryTitle;
    private String pantryUUID;
    private String pantryCategory;
    private ArrayList<String> itemList;
    private ArrayList<String> itemUUIDList;
    private ArrayList<String> itemStatus;

    private String currentSortingOrder;
    private Button sortingMethod;
    private Button mAddItemButton;
    private int itemClickedPosition;

    public static final int MAX_ITEM_NAME_LENGTH = 11;
    public static final int FULL = 1;
    public static final int LOW = 2;
    public static final int EMPTY = 3;
    public static final int EXPIRED = 4;

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
        sortingMethod.setBackgroundResource(R.drawable.alphabetical_selector);
        sortingMethod.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                switch(currentSortingOrder)
                {
                    case "A-Z":
                        currentSortingOrder = "STATUS";
                        sortingMethod.setBackgroundResource(R.drawable.status_sort_selector);

                        sortItemsByStatus();
                        setUpRecyclerView();
                        break;
                    case "STATUS":
                        currentSortingOrder = "EXP";
                        sortingMethod.setBackgroundResource(R.drawable.exp_sort_selector);
                        sortItemsByExpDate();
                        setUpRecyclerView();
                        break;
                    case "EXP":
                        currentSortingOrder = "A-Z";
                        sortingMethod.setBackgroundResource(R.drawable.alphabetical_selector);
                        sortItemsAlphabetically();
                        setUpRecyclerView();
                        break;
                }
            }
        });

        initializeArrayList();

        detailsLayout = findViewById(R.id.detailsLayout);
        detailsLayout.setVisibility(View.GONE);
        itemNameTextView = findViewById(R.id.itemNameTextView);
        itemExpirationTextView = findViewById(R.id.itemExpirationTextView);

        statusSetter1 = findViewById(R.id.statusSetterButton1);
        statusSetter2 = findViewById(R.id.statusSetterButton2);

        Button closeDetailsButton = (Button) findViewById(R.id.closeInfoButton);
        closeDetailsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                detailsLayout.setVisibility(View.GONE);
            }
        });

        Button editItemButton = (Button) findViewById(R.id.editItemButton);
        editItemButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String[] projection = {ItemTable.Cols.DATE};
                String selection = ItemTable.Cols.UUID + " = ?";
                String[] selectionValue = {itemUUIDList.get(itemClickedPosition)};

                Cursor cursor = readableDatabase.query(ItemTable.NAME, projection, selection, selectionValue, null, null, null);
                cursor.moveToNext();

                ArrayList<String> itemDetails = new ArrayList<>();
                itemDetails.add(itemList.get(itemClickedPosition));
                itemDetails.add(itemUUIDList.get(itemClickedPosition));
                itemDetails.add(cursor.getString(cursor.getColumnIndex(ItemTable.Cols.DATE)));

                Bundle bundle = new Bundle();
                bundle.putBoolean("EDIT_ITEM", true);
                bundle.putStringArrayList("NAME_UUID_DATE", itemDetails);

                FragmentManager manager = getSupportFragmentManager();
                AddItemFragment dialog = new AddItemFragment();
                dialog.setArguments(bundle);
                dialog.show(manager, DIALOG_ADD_ITEM);

                cursor.close();
            }
        });

        ItemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        setUpRecyclerView();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mAddItemButton = (Button) findViewById(R.id.addItemButton);
        mAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("EDIT_ITEM", false);
                FragmentManager manager = getSupportFragmentManager();
                AddItemFragment dialog = new AddItemFragment();
                dialog.setArguments(bundle);
                dialog.show(manager,DIALOG_ADD_ITEM);
            }
        });
    }

    public void setUpRecyclerView()
    {
        int numberOfColumns = 3;
        ItemsRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, itemList, itemStatus);
        adapter.setClickListener(this);
        ItemsRecyclerView.setAdapter(adapter);
    }

    public void initializeArrayList()
    {
        switch(currentSortingOrder)
        {
            case "A-Z":
                sortItemsAlphabetically();
                break;

            case "STATUS":
                sortItemsByStatus();
                break;

            case "EXP":
                sortItemsByExpDate();
                break;
        }
    }

    public Cursor getItemDBSortedAlphabetically()
    {
        String[] selectionArgs = {pantryUUID, pantryCategory};
        return readableDatabase.rawQuery("SELECT " + ItemTable.Cols.NAME + " FROM " + ItemTable.NAME + " WHERE " + ItemTable.Cols.PANTRY_ID + " LIKE ? " + " AND " + ItemTable.Cols.CATEGORY + " LIKE ? " + " ORDER BY " + ItemTable.Cols.NAME + " COLLATE NOCASE ASC;", selectionArgs);
    }

    public Cursor getItemUUIDSortedAlphabetically()
    {
        String[] selectionArgs = {pantryUUID, pantryCategory};
        return readableDatabase.rawQuery("SELECT " + ItemTable.Cols.UUID + " FROM " + ItemTable.NAME + " WHERE " + ItemTable.Cols.PANTRY_ID + " LIKE ? " + " AND " + ItemTable.Cols.CATEGORY + " LIKE ? " + " ORDER BY " + ItemTable.Cols.NAME + " COLLATE NOCASE ASC;", selectionArgs);
    }

    public Cursor getItemStatusSortedAlphabetically()
    {
        String[] selectionArgs = {pantryUUID, pantryCategory};
        return readableDatabase.rawQuery("SELECT " + ItemTable.Cols.STATUS + " FROM " + ItemTable.NAME + " WHERE " + ItemTable.Cols.PANTRY_ID + " LIKE ? " + " AND " + ItemTable.Cols.CATEGORY + " LIKE ? " + " ORDER BY " + ItemTable.Cols.NAME + " COLLATE NOCASE ASC;", selectionArgs);
    }

    public Cursor getItemDBSortedByStatus()
    {
        String[] projection = {ItemTable.Cols.NAME, ItemTable.Cols.UUID, ItemTable.Cols.STATUS};
        String selection = ItemTable.Cols.PANTRY_ID + " = ?" + " AND " + ItemTable.Cols.CATEGORY + " = ?";
        String[] selectValues = {pantryUUID, pantryCategory};
        String sortOrder = ItemTable.Cols.STATUS + " ASC";

        return readableDatabase.query(ItemTable.NAME, projection, selection, selectValues, null, null, sortOrder);

    }

    public Cursor getItemDBSortedByExpDate()
    {
        String[] projection = {ItemTable.Cols.NAME, ItemTable.Cols.UUID, ItemTable.Cols.STATUS};
        String selection = ItemTable.Cols.PANTRY_ID + " = ?" + " AND " + ItemTable.Cols.CATEGORY + " = ?";
        String[] selectValues = {pantryUUID, pantryCategory};
        String sortOrder = ItemTable.Cols.DATE + " ASC";

        return readableDatabase.query(ItemTable.NAME, projection, selection, selectValues, null, null, sortOrder);
    }

    public void sortItemsAlphabetically()
    {
        itemList = new ArrayList<>();
        itemUUIDList = new ArrayList<>();
        itemStatus = new ArrayList<>();

        Cursor itemNameCursor = getItemDBSortedAlphabetically();
        Cursor itemUUIDCursor = getItemUUIDSortedAlphabetically();
        Cursor itemStatusCursor = getItemStatusSortedAlphabetically();

        while(itemNameCursor.moveToNext() && itemUUIDCursor.moveToNext() && itemStatusCursor.moveToNext())
        {
            itemList.add(itemNameCursor.getString(itemNameCursor.getColumnIndex(ItemTable.Cols.NAME)));
            itemUUIDList.add(itemUUIDCursor.getString(itemUUIDCursor.getColumnIndex(ItemTable.Cols.UUID)));
            itemStatus.add(itemStatusCursor.getString(itemStatusCursor.getColumnIndex(ItemTable.Cols.STATUS)));
        }
        itemNameCursor.close();
        itemUUIDCursor.close();
        itemStatusCursor.close();

    }

    public void sortItemsByExpDate()
    {
        itemList = new ArrayList<>();
        itemUUIDList = new ArrayList<>();
        itemStatus = new ArrayList<>();

        Cursor cursor = getItemDBSortedByExpDate();

        while(cursor.moveToNext())
        {
            itemList.add(cursor.getString(cursor.getColumnIndex(ItemTable.Cols.NAME)));
            itemUUIDList.add(cursor.getString(cursor.getColumnIndex(ItemTable.Cols.UUID)));
            itemStatus.add(cursor.getString(cursor.getColumnIndex(ItemTable.Cols.STATUS)));
        }

        cursor.close();

    }

    public void sortItemsByStatus()
    {
        itemList = new ArrayList<>();
        itemUUIDList = new ArrayList<>();
        itemStatus = new ArrayList<>();

        Cursor cursor = getItemDBSortedByStatus();

        while(cursor.moveToNext())
        {
            itemList.add(cursor.getString(cursor.getColumnIndex(ItemTable.Cols.NAME)));
            itemUUIDList.add(cursor.getString(cursor.getColumnIndex(ItemTable.Cols.UUID)));
            itemStatus.add(cursor.getString(cursor.getColumnIndex(ItemTable.Cols.STATUS)));
        }

        cursor.close();
    }

    public boolean checkForDuplicateItem(String ItemName)
    {
        boolean foundItem = false;
        String[] selectionArgs = {pantryUUID, ItemName};

        Cursor cursor = readableDatabase.rawQuery("SELECT " + ItemTable.Cols.NAME + " FROM "+ ItemTable.NAME + " WHERE " + ItemTable.Cols.PANTRY_ID + " LIKE ?" + " AND " + ItemTable.Cols.NAME + " LIKE ?;",selectionArgs);

        if(cursor.moveToNext() && cursor.getString(cursor.getColumnIndex(ItemTable.Cols.NAME)).toLowerCase().equals(ItemName.toLowerCase()))
        {
            foundItem = true;
        }
        cursor.close();

        return foundItem;
    }

    @Override
    public void AddItem(String newItemName, Boolean expirable, int expirationMonth, int expirationDay, int expirationYear)
    {
        if(newItemName.length() > MAX_ITEM_NAME_LENGTH)
        {
            Toast.makeText(getApplicationContext(), "Item name is too long!", Toast.LENGTH_SHORT).show();
        }
        else if(checkForDuplicateItem(newItemName))
        {
            Toast.makeText(getApplicationContext(), newItemName + " already exists!", Toast.LENGTH_SHORT).show();
        }
        else if(newItemName.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Can't have a blank name", Toast.LENGTH_SHORT).show();
        }
        else {
            ContentValues values = new ContentValues();
            values.put(ItemTable.Cols.UUID, UUID.randomUUID().toString());
            values.put(ItemTable.Cols.NAME, newItemName);
            values.put(ItemTable.Cols.DATE, expirationYear + "/" + (expirationMonth + 1) + "/" + expirationDay);
            values.put(ItemTable.Cols.PANTRY_ID, this.pantryUUID);
            values.put(ItemTable.Cols.CATEGORY, this.pantryCategory);
            values.put(ItemTable.Cols.STATUS, FULL);

            writableDatabase.insert(ItemTable.NAME, null, values);
            initializeArrayList();
            setUpRecyclerView();
        }
    }

    @Override
    public void onItemClick(View view, int position)
    {
        final View tempView = view;
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);

        itemClickedPosition = position;

        String[] projection = {ItemTable.Cols.NAME, ItemTable.Cols.DATE, ItemTable.Cols.STATUS};
        String selection = ItemTable.Cols.UUID + " = ? ";
        String[] selectionVals = {itemUUIDList.get(position)};
        Cursor cursor = readableDatabase.query(ItemTable.NAME,projection,selection,selectionVals, null, null, null);
        cursor.moveToNext();
        int iNameColumn = cursor.getColumnIndex(ItemTable.Cols.NAME);
        int iDateColumn = cursor.getColumnIndex(ItemTable.Cols.DATE);
        int iStatusColumn = cursor.getColumnIndex(ItemTable.Cols.STATUS);

        String expDate = cursor.getString(iDateColumn);

        itemNameTextView.setText(cursor.getString(iNameColumn));
        if(expDate.equals("0/0/0"))
            expDate = "Not Perishable";

        itemExpirationTextView.setText(expDate);
        int itemStatus = cursor.getInt(iStatusColumn);

        switch(itemStatus)
        {
            case FULL:
                statusSetter1.setBackgroundResource(R.drawable.lowstatus);
                statusSetter1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        itemModifyStatus(tempView, itemClickedPosition, LOW);
                        detailsLayout.setVisibility(View.GONE);
                    }
                });
                statusSetter2.setBackgroundResource(R.drawable.emptystatus);
                statusSetter2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        itemModifyStatus(tempView, itemClickedPosition, EMPTY);
                        detailsLayout.setVisibility(View.GONE);
                    }
                });
                break;
            case LOW:
                statusSetter1.setBackgroundResource(R.drawable.fullstatus);
                statusSetter1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        itemModifyStatus(tempView, itemClickedPosition, FULL);
                        detailsLayout.setVisibility(View.GONE);
                    }
                });
                statusSetter2.setBackgroundResource(R.drawable.emptystatus);
                statusSetter2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        itemModifyStatus(tempView, itemClickedPosition, EMPTY);
                        detailsLayout.setVisibility(View.GONE);
                    }
                });
                break;
            case EMPTY:
                statusSetter1.setBackgroundResource(R.drawable.fullstatus);
                statusSetter1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        itemModifyStatus(tempView, itemClickedPosition, FULL);
                        detailsLayout.setVisibility(View.GONE);
                    }
                });
                statusSetter2.setBackgroundResource(R.drawable.lowstatus);
                statusSetter2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        itemModifyStatus(tempView, itemClickedPosition, LOW);
                        detailsLayout.setVisibility(View.GONE);
                    }
                });
                break;
        }
        detailsLayout.setVisibility(View.VISIBLE);
    }



    @Override
    public void deleteItem(View view, int position)
    {
        String selectionForItemTable = ItemTable.Cols.UUID + " LIKE ?";
        String[] whereValue = { itemUUIDList.get(position)};

        writableDatabase.delete(ItemTable.NAME, selectionForItemTable, whereValue);
        initializeArrayList();
        setUpRecyclerView();

    }
    @Override
    public void itemModifyStatus(View view, int position, int newStatus)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemTable.Cols.STATUS, newStatus);
        writableDatabase.update(ItemTable.NAME, contentValues, ItemTable.Cols.UUID + "=?", new String[] {itemUUIDList.get(position)});
        initializeArrayList();
        setUpRecyclerView();
    }

    @Override
    public void editItem(String itemName, Boolean expirable, int expirationMonth, int expirationDay, int expirationYear, String itemUUID)
    {
        if(itemName.length() > MAX_ITEM_NAME_LENGTH)
        {
            Toast.makeText(getApplicationContext(), "Item name is too long!", Toast.LENGTH_SHORT).show();
        }
        else if(itemName.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Can't have a blank name", Toast.LENGTH_SHORT).show();
        }
        else {
            ContentValues values = new ContentValues();
            values.put(ItemTable.Cols.NAME, itemName);
            values.put(ItemTable.Cols.DATE, expirationYear + "/" + (expirationMonth + 1) + "/" + expirationDay);

            String selection = ItemTable.Cols.UUID + " = ?";
            String[] selectionValues = {itemUUID};

            writableDatabase.update(ItemTable.NAME, values, selection, selectionValues);

            initializeArrayList();
            setUpRecyclerView();
        }

        detailsLayout.setVisibility(View.GONE);
    }

}
