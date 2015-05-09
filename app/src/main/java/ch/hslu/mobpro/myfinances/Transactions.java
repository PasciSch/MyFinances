package ch.hslu.mobpro.myfinances;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Transactions extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new LoadTransactionTask().execute();
    }

    private class LoadTransactionTask extends AsyncTask<Void, Void, Void>
    {
        private List<AccountDto> accountDtos;
        private List<CategoryDto> categoryDtos;

        @Override
        protected Void doInBackground(Void... params) {
            DbAdapter adapter = new DbAdapter(Transactions.this);
            adapter.open();
            this.accountDtos = adapter.getAccounts();
            this.categoryDtos = adapter.getCategories();
            adapter.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Transactions.this);
            final String defaultNumberKey = getString(R.string.persistence_key_defaultNumber);
            final String defaultAccountKey = getString(R.string.persistence_key_accountPreference);
            final String defaultCategoryKey = getString(R.string.persistence_key_categoryPreference);
            String defaultNumberString = preferences.getString(defaultNumberKey, "0");
            String defaultAccount = preferences.getString(defaultAccountKey, "1");
            String defaultCategory = preferences.getString(defaultCategoryKey, "1");

            EditText dateEditText = (EditText)findViewById(R.id.entry_date);
            SimpleDateFormat format = new SimpleDateFormat(TransactionDto.DateFormat);
            dateEditText.setText(format.format(new Date()));

            ArrayAdapter<AccountDto> accountAdapter = new ArrayAdapter<>(Transactions.this, android.R.layout.simple_list_item_1, accountDtos);
            Spinner accountSpinner = (Spinner)findViewById(R.id.entry_account);
            accountSpinner.setAdapter(accountAdapter);
            accountSpinner.setSelection(Integer.parseInt(defaultAccount)-1); // Todo:don't expect id and order to be the same

            ArrayAdapter<CategoryDto> categoryAdapter = new ArrayAdapter<>(Transactions.this, android.R.layout.simple_list_item_1, categoryDtos);
            Spinner categorySpinner = (Spinner)findViewById(R.id.entry_categorie);
            categorySpinner.setAdapter(categoryAdapter);
            categorySpinner.setSelection(Integer.parseInt(defaultCategory)-1); // Todo:don't expect id and order to be the same

            EditText amountEditText = (EditText)findViewById(R.id.entry_amount);
            amountEditText.setText(defaultNumberString);
        }
    }

    public void navigateBack(View view) {
        this.finish();
    }

    public void saveEntry(View view) {
        EditText dateEditText = (EditText)findViewById(R.id.entry_date);
        EditText amountEditText = (EditText)findViewById(R.id.entry_amount);
        EditText descriptionEditText = (EditText)findViewById(R.id.entry_description);
        Spinner categorySpinner = (Spinner)findViewById(R.id.entry_categorie);
        Spinner accountSpinner = (Spinner)findViewById(R.id.entry_account);

        try {
            SimpleDateFormat format = new SimpleDateFormat(TransactionDto.DateFormat);
            TransactionDto transaction = new TransactionDto();
            transaction.setDate(new java.sql.Date(format.parse(dateEditText.getText().toString()).getTime())); //temp bad
            transaction.setAmount(Float.parseFloat(amountEditText.getText().toString()));
            transaction.setDescription(descriptionEditText.getText().toString());
            transaction.setCategory((CategoryDto) categorySpinner.getSelectedItem());
            transaction.setAccount((AccountDto) accountSpinner.getSelectedItem());

            new StoreTransactionTask().execute(transaction);
        }
        catch (ParseException exception)
        {
            Toast.makeText(Transactions.this, "Invalid Date", Toast.LENGTH_SHORT).show();
        }
        catch (NumberFormatException exception)
        {
            Toast.makeText(Transactions.this, "Invalid Amount", Toast.LENGTH_SHORT).show();
        }
        catch (Exception exception)
        {
            Toast.makeText(Transactions.this, "Save failed", Toast.LENGTH_SHORT).show();
        }
    }

    private class StoreTransactionTask extends AsyncTask<TransactionDto, Void, Void>{

        @Override
        protected Void doInBackground(TransactionDto... params) {
            DbAdapter adapter = new DbAdapter(Transactions.this);
            adapter.open();
            adapter.insertTransaction(params[0]);
            adapter.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Transactions.this.finish();
        }
    }
}
