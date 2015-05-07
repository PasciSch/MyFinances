package ch.hslu.mobpro.myfinances;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DbAdapter {
    public static final String DB_Name = "MyFinacesDb";
    public static final int DB_Version = 1;

    public static final String ACCOUNTING_ENTRY_Table = "ACCOUNTINGENTRY";
    public static final String DATE_Entry = "DATE";
    public static final String AMOUNT_Entry = "AMOUNT";
    public static final String DESCRIPTION_Entry = "DESCRIPTION";
    public static final String ACCOUNT_Entry = "ACCOUNT";
    public static final String CATEGORY_Entry = "CATEGORY";
    public static final String ID_Entry = "ID";

    public static final String ACCOUNT_Table = "ACCOUNT";
    public static final String ID_Account = "ID";
    public static final String NAME_Account = "NAME";

    public static final String CATEGORY_Table = "CATEGORY";
    public static final String ID_Category = "ID";
    public static final String NAME_Category = "NAME";
    public static final String TYPE_Category = "TYPE";

    private final DbHelper dbHelper;
    private SQLiteDatabase db;

    public DbAdapter(final Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() {
        if (db == null || !db.isOpen()){
            db = dbHelper.getWritableDatabase();
        }
    }

    public void close() {
        dbHelper.close();
    }

    public void insertTransaction(final TransactionDto transaction)
    {
        final ContentValues values = new ContentValues();
        values.put(DATE_Entry, transaction.getDate().getTime());
        values.put(AMOUNT_Entry, transaction.getAmount());
        values.put(DESCRIPTION_Entry, transaction.getDescription());
        values.put(ACCOUNT_Entry, transaction.getAccount().getId());
        values.put(CATEGORY_Entry, transaction.getCategory().getId());
        final long id = db.insert(ACCOUNTING_ENTRY_Table, null, values);
        transaction.setId(id);
    }

    public List<TransactionDto> getAllTransactions()
    {
        List<TransactionDto> transaction = new ArrayList<>();
        final Cursor result = db.query(
                ACCOUNTING_ENTRY_Table,
                new String[] {
                        ID_Entry,
                        DATE_Entry,
                        AMOUNT_Entry,
                        DESCRIPTION_Entry,
                        ACCOUNT_Entry,
                        CATEGORY_Entry
                },
                null, null, null, null, null);
        boolean found = result.moveToFirst();
        while (found)
        {
            transaction.add(getNextTransaction(result));
            found = result.moveToNext();
        }
        result.close();
        return transaction;
    }

    public List<TransactionDto> getTransactionsOfAccount(long accountId)
    {
        List<TransactionDto> transaction = new ArrayList<>();
        final Cursor result = db.query(
                ACCOUNTING_ENTRY_Table,
                new String[] {
                        ID_Entry,
                        DATE_Entry,
                        AMOUNT_Entry,
                        DESCRIPTION_Entry,
                        ACCOUNT_Entry,
                        CATEGORY_Entry
                },
                ACCOUNT_Entry + "=" + accountId,
                null, null, null, null);
        boolean found = result.moveToFirst();
        while (found)
        {
            transaction.add(getNextTransaction(result));
            found = result.moveToNext();
        }
        result.close();
        return transaction;
    }

    private TransactionDto getNextTransaction(Cursor cursor) {
        final TransactionDto transaction = new TransactionDto();
        transaction.setId(cursor.getFloat(0));
        transaction.setDate(new java.sql.Date(cursor.getLong(1)));
        transaction.setAmount(cursor.getFloat(2));
        transaction.setDescription(cursor.getString(3));

        transaction.setAccount(this.getAccountById(cursor.getLong(4)));
        transaction.setCategory(this.getCategoryById(cursor.getLong(5)));

        return transaction;
    }

    public List<AccountDto> getAccounts()
    {
        List<AccountDto> accounts = new ArrayList<>();
        final Cursor result = db.query(
                ACCOUNT_Table,
                new String[] {
                        ID_Account,
                        NAME_Account
                },
                null, null, null, null, null);
        boolean found = result.moveToFirst();
        while (found)
        {
            accounts.add(getNextAccount(result));
            found = result.moveToNext();
        }
        result.close();
        return accounts;
    }

    public AccountDto getAccountById(final long id)
    {
       AccountDto account = null;
        final Cursor result = db.query(
                ACCOUNT_Table,
                new String[] {
                        ID_Account,
                        NAME_Account,
                },
                ID_Account + "=" + id,
                null, null, null, null);
        boolean found = result.moveToFirst();
        if (found)
        {
            account = getNextAccount(result);
        }
        result.close();
        return account;
    }

    private AccountDto getNextAccount(Cursor cursor) {
        final AccountDto account = new AccountDto();
        account.setId(cursor.getLong(0));
        account.setName(cursor.getString(1));
        return account;
    }

    public List<CategoryDto> getCategories()
    {
        List<CategoryDto> categories = new ArrayList<>();
        final Cursor result = db.query(
                CATEGORY_Table,
                new String[] {
                        ID_Category,
                        NAME_Category,
                        TYPE_Category
                },
                null, null, null, null, null);
        boolean found = result.moveToFirst();
        while (found)
        {
            categories.add(getNextCategory(result));
            found = result.moveToNext();
        }
        result.close();
        return categories;
    }

    public CategoryDto getCategoryById(final long id)
    {
        CategoryDto category = null;
        final Cursor result = db.query(
                CATEGORY_Table,
                new String[] {
                        ID_Category,
                        NAME_Category,
                        TYPE_Category
                },
                ID_Category + "=" + id,
                null, null, null, null);
        boolean found = result.moveToFirst();
        if (found)
        {
            category = getNextCategory(result);
        }
        result.close();
        return category;
    }

    private CategoryDto getNextCategory(Cursor cursor) {
        final CategoryDto category = new CategoryDto();
        category.setId(cursor.getLong(0));
        category.setName(cursor.getString(1));
        category.setGain(cursor.getLong(2) == 1);
        return category;
    }
}
