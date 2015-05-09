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

        this.insertAccount(db, "Bargeld");
        this.insertAccount(db, "LUKB Priv.");
        this.insertAccount(db, "Postfinance");
        this.insertAccount(db, "VISA Kred.");

        this.insertSpendCategory(db, "Essen");
        this.insertSpendCategory(db, "Parking");
        this.insertSpendCategory(db, "Ausgang");
        this.insertSpendCategory(db, "Arzt");
        this.insertSpendCategory(db, "Ausleihen");
        this.insertSpendCategory(db, "Studium");
        this.insertSpendCategory(db, "Kontoübertrag");

        this.insertGainCategory(db, "Lohn");
        this.insertGainCategory(db, "Geschenk");
        this.insertGainCategory(db, "Schuldrückzahlung");
        this.insertGainCategory(db, "Kontoübertrag");
    }

    private void insertGainCategory(SQLiteDatabase db, String name)
    {
        db.execSQL("INSERT INTO CATEGORY(ID,NAME,TYPE)" +
                "VALUES (NULL,'" + name + "', 1)");
    }

    private void insertSpendCategory(SQLiteDatabase db, String name)
    {
        db.execSQL("INSERT INTO CATEGORY(ID,NAME,TYPE)" +
                "VALUES (NULL,'" + name + "', -1)");
    }

    private void insertAccount(SQLiteDatabase db, String name)
    {
        db.execSQL("INSERT INTO ACCOUNT(ID,NAME)" +
                "VALUES (NULL,'" + name + "')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new RuntimeException("Not jet implemented");
    }
}
