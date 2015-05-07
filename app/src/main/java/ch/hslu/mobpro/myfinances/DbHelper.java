package ch.hslu.mobpro.myfinances;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, DbAdapter.DB_Name, null, DbAdapter.DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ACCOUNTINGENTRY(" +
                "ID INTEGER PRIMARY KEY," +
                "AMOUNT REAL NOT NULL," +
                "DESCRIPTION TEXT NULL," +
                "DATE DATE NOT NULL," +
                "ACCOUNT INTEGER NOT NULL," +
                "CATEGORY INTEGER NOT NULL" +
                ")");
        db.execSQL("CREATE TABLE ACCOUNT(" +
                "ID INTEGER PRIMARY KEY," +
                "NAME TEXT NOT NULL" +
                ")");
        db.execSQL("CREATE TABLE CATEGORY(" +
                "ID INTEGER PRIMARY KEY," +
                "NAME TEXT NOT NULL," +
                "TYPE INTEGER NOT NULL" +
                ")");
        db.execSQL("INSERT INTO ACCOUNT(ID,NAME)" +
                "VALUES (NULL,'Lohnkonto')");
        db.execSQL("INSERT INTO ACCOUNT(ID,NAME)" +
                "VALUES (NULL,'Bargeld')");
        db.execSQL("INSERT INTO CATEGORY(ID,NAME,TYPE)" +
                "VALUES (NULL,'ESSEN', -1)");
        db.execSQL("INSERT INTO CATEGORY(ID,NAME,TYPE)" +
                "VALUES (NULL,'Lohn', 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new RuntimeException("Not jet implemented");
    }
}
