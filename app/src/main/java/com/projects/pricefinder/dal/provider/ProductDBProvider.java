package com.projects.pricefinder.dal.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.content.Context;

import com.projects.pricefinder.models.Product;

/**
 * Created by Administrator on 02/28/2015.
 */
public class ProductDBProvider extends ContentProvider {

    private Product product;
    static final String PROVIDER_NAME =
            "com.projects.pricefinder.dal.provider";
    static final Uri CONTENT_URI =
            Uri.parse("content://" + PROVIDER_NAME + "/products");

    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String PRICECURRENCY = "pricecurrency";
    public static final String SUPPLIER = "supplier";
    public static final String DESC = "desc";
    public static final String URL = "url";
    public static final String IMAGEURL = "imageUrl";
    public static final String DATECREATED = "dateCreated";
    public static final String INTEREST = "interest";

    private static final UriMatcher uriMatcher;
    static final int Products = 1;
    static final int PRODUCT_ID = 2;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "products", Products);
        uriMatcher.addURI(PROVIDER_NAME, "products/#", PRODUCT_ID);
    }

    //---for database use---
    SQLiteDatabase pfDB;
    static final String DATABASE_NAME = "pricefinder.db";
    static final String DATABASE_TABLE = "Product";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE ="";
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            Log.w("Content provider database",
                    "Upgrading database from version " +
                    oldVersion + " to " + newVersion +
                    ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        pfDB = dbHelper.getWritableDatabase();
        return (pfDB == null)? false:true;
    }
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            //---get all ---
            case Products:
                return "vnd.android.cursor.dir/vnd.com.projects.pricefinder.products ";

            //---get a particular one---
            case PRODUCT_ID:
                return "vnd.android.cursor.item/vnd.com.projects.pricefinder.products ";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    //--need to complete-02/28
    public Uri insert(Uri uri, ContentValues values) {
        //---add a new PRODUCT---
        long rowID = pfDB.insert(
                DATABASE_TABLE,
                "",
                values);

        Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        return  _uri;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(DATABASE_TABLE);


        if (uriMatcher.match(uri) == PRODUCT_ID)
            //---if getting a particular PRODUCT---
            sqlBuilder.appendWhere(
                    _ID + " = " + uri.getPathSegments().get(1));

        if (sortOrder==null || sortOrder=="")
            sortOrder = NAME;

        Cursor c = sqlBuilder.query(
                pfDB,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        //---register to watch a content URI for changes---
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case Products:
                count = pfDB.update(
                        DATABASE_TABLE,
                        values,
                        selection,
                        selectionArgs);
                break;
            case PRODUCT_ID:
                String id = uri.getPathSegments().get(1);
                try{
                    if(Integer.parseInt(id)>0 &&
                            !values.get(NAME).toString().isEmpty() &&
                            !values.get(_ID).toString().isEmpty() &&
                            !values.get(DESC).toString().isEmpty()){
                        count = pfDB.update(
                                DATABASE_TABLE,
                                values,
                                _ID + " = " + uri.getPathSegments().get(1) +
                                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                                selection + ')' : ""),
                                selectionArgs);
                    }
                }
                catch(NumberFormatException e){
                    throw new NumberFormatException ("Course Info is not input correctly. Invalid Id or value(s) are empty.");
                }
                break;
            default: throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        // arg0 = uri
        // arg1 = selection
        // arg2 = selectionArgs
        int count=0;

        String id = arg0.getPathSegments().get(1);

        switch (uriMatcher.match(arg0)){
            case Products:

                count = pfDB.delete(
                        DATABASE_TABLE,
                        arg1,
                        arg2);
                break;
            case PRODUCT_ID:
                //String id = arg0.getPathSegments().get(1);
                try{
                    if (Integer.parseInt(id)>0){
                        count = pfDB.delete(
                                DATABASE_TABLE,
                                _ID + " = " + id +
                                        (!TextUtils.isEmpty(arg1) ? " AND (" +
                                                arg1 + ')' : ""),
                                arg2);
                    }
                }
                catch(NumberFormatException e){
                    throw new NumberFormatException ("Unknown URI "+ arg0);
                }
                break;
            default: throw new IllegalArgumentException("Delete Failed, unknown URI " + arg0);
        }
        getContext().getContentResolver().notifyChange(arg0, null);
        return count;
    }



}
