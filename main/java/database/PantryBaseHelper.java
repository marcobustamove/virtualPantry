package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import database.PantryDBSchema.PantryTable;
import database.PantryDBSchema.ItemTable;

public class PantryBaseHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "PantryDatabase.db";

    public PantryBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //For Pantries
        db.execSQL("create table " + PantryTable.NAME + "(" +
        " _id integer primary key autoincrement, " +
                PantryTable.Cols.UUID + ", " +
                PantryTable.Cols.TITLE + ")");

        //For Items
        db.execSQL("create table " +
                ItemTable.NAME + "(" + " _id integer primary key autoincrement, " +
                ItemTable.Cols.UUID + ", " +
                ItemTable.Cols.NAME + ", " +
                ItemTable.Cols.DATE + ", " +
                ItemTable.Cols.STATUS + ", " +
                ItemTable.Cols.PANTRY_ID + ", " +
                ItemTable.Cols.CATEGORY + ", " +
                ItemTable.Cols.IMG_REF + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
