package com.mobilesiri.sqliteexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper /* abstract class */ {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "shopsInfo";

    // Contacts table name
    private static final String TABLE_SHOPS = "shops";

    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SH_ADDR = "shop_address";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        /* Initializes in SqliteOpenHelper:
         mContext = context;
        mName = name;
        mFactory = factory;
        mNewVersion = version;
        mErrorHandler = errorHandler;
         */
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SHOPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SH_ADDR + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPS);
        // Creating tables again
        onCreate(db);
    }

    // Adding new shop
    public void addShop(Shop shop /* shop is fully ready-made. Dependency injected */) {
        SQLiteDatabase db = this.getWritableDatabase(); /*Creates database or opens it if already created. This is a thread safe method */

        /*  ContentValues are parcelable objects used to store a set of values that the ContentResolver can process */
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, shop.getName()); // putting values based on Map
        values.put(KEY_SH_ADDR, shop.getAddress());

        // Inserting Row
        db.insert(TABLE_SHOPS, null, values); //values insertion on table

        //destroy the db by closing. I don't need to call db.close. It is not needed because getWritableDatabase gives me synchronized database */
        db.close(); // Closing database connection. db is calling close(), it means there should be Closeable interface implemented by db's class


        /* The SqliteOpenHelper object holds on to one database connection. It appears to offer you a read and write connection, but it really doesn't. Call the read-only, and you'll get the write database connection regardless.

So, one helper instance, one db connection. Even if you use it from multiple threads, one connection at a time. The SqliteDatabase object uses java locks to keep access serialized. So, if 100 threads have one db instance, calls to the actual on-disk database are serialized.*/



        /*SQLiteDatabase db = this.getWritableDatabase();
//        db.close();
        These above two lines: I am opening/creating database and then I am closing always. I want to open/create just one
        and call close in stop or destroy of my activity. This saves my processor and space.*/

    }

    // Getting one shop
    public Shop getShop(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SHOPS, new String[]{KEY_ID,
                        KEY_NAME, KEY_SH_ADDR}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Shop contact = new Shop(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return shop
        return contact;
    }

    // Getting All Shops
    public List<Shop> getAllShops() {
        /*
        I fetch the data from database, update to model, and return the model
         */
        List<Shop> shopList = new ArrayList<Shop>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SHOPS;

        SQLiteDatabase db = this.getWritableDatabase(); /* I am not initializing the db as global variable because:
        1) It should be thread-safe, it calls acquireReference() and releaseReference() after I complete my operation on
        the database */
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Shop shop = new Shop();
                shop.setId(Integer.parseInt(cursor.getString(0)));
                shop.setName(cursor.getString(1));
                shop.setAddress(cursor.getString(2));
                // Adding contact to list
                shopList.add(shop);
            } while (cursor.moveToNext());
        }

        // return contact list
        return shopList;
    }

    // Getting shops Count
    public int getShopsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SHOPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating a shop
    public int updateShop(Shop shop) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, shop.getName());
        values.put(KEY_SH_ADDR, shop.getAddress());

        // updating row
        return db.update(TABLE_SHOPS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(shop.getId())});
    }

    // Deleting a shop
    public void deleteShop(Shop shop) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SHOPS, KEY_ID + " = ?",
                new String[] { String.valueOf(shop.getId()) });
        db.close();
    }
}

