package e.natasja.natasjawezel__pset4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Natasja on 20-11-2017.
 */

public class TodoDatabase extends SQLiteOpenHelper {

    // this is where the unique instance of the class is stored, once made
    private static TodoDatabase instance = null;

    // some variables that we'll use often
    private static final String TABLE_NAME = "todos";
    private static final String COL1 = "_id";
    private static final String COL2 = "title";
    private static final String COL3 = "completed";

    // constructor of the class
    private TodoDatabase(Context context) {
        super(context, "todos", null, 5);
    }

    // methods of the class

    /**
     * on create, creates a database with the columns id, title and completed
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table " + TABLE_NAME + " (" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, " + COL3 + " BOOLEAN)";
        db.execSQL(createTable);
    }

    /**
     * on upgrade, drops the old database and creates a new one, with the columns id, title and
     * completed.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "todos");
        onCreate(db);
    }

    /**
     * because the database is a singleton, the constructor should be private. therefore you can't
     * construct a database from the mainactivity file. this function returns to you the (only)
     * instance of this database, that is created in thís file.
     * @param context
     * @return
     */
    public static TodoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new TodoDatabase(context);
        }
        return instance;
    }

    /**
     * Returns all the data from the database
     * @return
     */
    public Cursor selectAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null, null);
        return data;
    }

    public void update(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "UPDATE " + TABLE_NAME + " SET " + COL3 + " = (CASE " + COL3 + " WHEN 1 THEN 0 ELSE 1 END) WHERE " + COL1 + " = " + id;
        db.execSQL(query);

    }

    /**
     * Adds data with a custom text value to the tododatabase, the ID autoincrements and the
     * boolean is normally false.
     * @param thingTODO
     * @return
     */
    public boolean addData(String thingTODO) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, thingTODO);
        contentValues.put(COL3, 1);

        Log.d("MainActivity", "addData; Adding " + thingTODO + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);

        // if data inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * when given an ID (from the function getItemID) , deletes the query with that itemID
     * @param id
     */
    public void deleteData(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("MainActivity", "Deleting: where id = " + id);

        String query = "DELETE from " + TABLE_NAME + " WHERE "
                + COL1 + " = " + id;
        db.execSQL(query);
    }

}
