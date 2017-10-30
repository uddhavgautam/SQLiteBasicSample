package com.mobilesiri.sqliteexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* Initializes in SqliteOpenHelper:
        mContext = context;
        mName = name;
        mFactory = factory;
        mNewVersion = version;
        mErrorHandler = errorHandler;
         */
        DBHandler /* SqliteOpenHelper class */ db = new DBHandler(this);

        // Inserting Shop/Rows
        Log.d("Insert: ", "Inserting ..");
        /* addshop() does: get the database, get all the contentValues from shop, insert contentValue, and finally closes the database */
        db.addShop(new Shop("Dockers", " 475 Brannan St #330, San Francisco, CA 94107, United States"));
        db.addShop(new Shop("Dunkin Donuts", "White Plains, NY 10601"));
        db.addShop(new Shop("Pizza Porlar", "North West Avenue, Boston , USA"));
        db.addShop(new Shop("Town Bakers", "Beverly Hills, CA 90210, USA"));

        // Reading all shops
        Log.d("Reading: ", "Reading all shops..");
        List<Shop> shops = db.getAllShops(); /* this method sets the Id.

         How did I find?
         1) I manually scanned (checked) all the possible instantiations above fetching lines.
         Possibilities were: initialized (set of Id) either in db creation -- line 16
         or db.getAllShops(). Finally found inside the method stub of db.getAllShops()

         I don't want to manually scan (check) this shitty things. If there were thousands of lines above me then why?*/

        for (Shop shop : shops) {
            String log = "Id: " + shop.getId()
                    /* Question: I have not provided id above, but I am fetching id values also, how? */
                    + " ,Name: " + shop.getName() + " ,Address: " + shop.getAddress();
            // Writing shops  to log
            Log.d("Shop: : ", log);
        }
    }
}
