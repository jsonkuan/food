package iths.com.food;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by wilhelmfors on 14/11/16.
 */


public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "food.db";
    private static final String TABLE_NAME = "food_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "Name";
    private static final String COL_3 = "Picture";
    //public static final String COL_4 = "Thumbnail";
    //public static final String COL_5 = "Health_grade";
    //public static final String COL_6 = "Taste_grade";
    //public static final String COL_7 = "Coordinates";
    //public static final String COL_8 = "Category";
    //public static final String COL_9 = "Time";
    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_1 + " INTEGER PRIMARY KEY," +
                    COL_2 + TEXT_TYPE + "," +
                    COL_3 + TEXT_TYPE + " )";



    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insert(String id, String name, String photo) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        values.put(COL_1, id);
        values.put(COL_2, name);
        values.put(COL_3, photo);
        long ok = db.insert(TABLE_NAME, null, values);

        db.close();

        return ok;
    }

}
