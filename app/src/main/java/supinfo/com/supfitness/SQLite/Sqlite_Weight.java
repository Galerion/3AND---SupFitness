package supinfo.com.supfitness.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import supinfo.com.supfitness.Model.Weight;

public class Sqlite_Weight {
    private static final int DB_VERSION = 1;
    private static final String NAME_DB = "SupFitness.db";

    private static final String TABLE_WEIGHT = "WEIGHT";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_DATE = "DATE";
    private static final int NUM_COL_DATE = 1;
    private static final String COL_WEIGHT = "WEIGHT";
    private static final int NUM_COL_WEIGHT = 2;

    private SQLiteDatabase db;

    private Sqlite sqlite;

    public Sqlite_Weight(Context context){
        //On créer la BDD et sa table
        sqlite = new Sqlite(context, NAME_DB, null, DB_VERSION);
    }

    public void open(){
        //on ouvre la BDD en écriture
        db = sqlite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        db.close();
    }

    public SQLiteDatabase getBDD(){
        return db;
    }

    public void RemoveDB(){
        sqlite.onUpgrade(db,1,2);
    }
    public long insertWeight(Weight weight){
        ContentValues values = new ContentValues();
        //Ajoute une valeur associé à une clé (le nom de la colonne dans la BD)
        values.put(COL_DATE, weight.getDate());
        values.put(COL_WEIGHT, weight.getWeight().toString());
        //Insertion de l'objet dans la BD via le ContentValues
        return db.insert(TABLE_WEIGHT, null, values);
    }

    public int removeWeight(int id){
        //Suppression d'un poid dans la BD grâce à l'ID
        return db.delete(TABLE_WEIGHT, COL_ID + " = " + id, null);
    }

    public ArrayList<Weight> getAllWeights() {
        //Permet de récupérer tout les poids de la BD dans un ArrayList
        ArrayList<Weight> weightList = new ArrayList<Weight>();
        // Selection de tous les poids
        String selectQuery = "SELECT  * FROM " + TABLE_WEIGHT;

        SQLiteDatabase db = sqlite.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Boucle sur chaque élément du curseur, et l'ajoute dans l'ArrayList
        if (cursor.moveToFirst()) {
            do {
                Weight weight = new Weight();
                weight.setId(Integer.parseInt(cursor.getString(0)));
                weight.setDate(cursor.getString(1));
                weight.setWeight(cursor.getDouble(2));
                // Adding contact to list
                weightList.add(weight);
            } while (cursor.moveToNext());
        }
        return weightList;
    }

}
