package supinfo.com.supfitness.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Sqlite extends SQLiteOpenHelper {
    private static final String TABLE_WEIGHT = "WEIGHT";
    private static final String COL_ID = "ID";
    private static final String COL_DATE = "DATE";
    private static final String COL_WEIGHT = "WEIGHT";

    private static final String CREATE_DB = "CREATE TABLE " + TABLE_WEIGHT + " ("
            + COL_ID + " INTEGER PRIMARY KEY, " + COL_DATE + " TEXT NOT NULL, "
            + COL_WEIGHT + " REAL NOT NULL);";

    public Sqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creation de la table
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Suppression puis recréation de la table afin de repartir de zéro
        db.execSQL("DROP TABLE " + TABLE_WEIGHT + ";");
        onCreate(db);
    }
}
