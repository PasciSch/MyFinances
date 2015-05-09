package ch.hslu.mobpro.myfinances;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


public class MyFinances extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_finances);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String key = getString(R.string.persistence_key_fastTransactionAdd);
        boolean shouldNavigateToTransactions = preferences.getBoolean(key, false);
        if (shouldNavigateToTransactions)
        {
            this.navigateToTransaction(null);
        }

        ListView list = (ListView)findViewById(R.id.entrylist);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                TransactionDto transaction = (TransactionDto)adapter.getItemAtPosition(position);

                Intent transactionIntent = new Intent(MyFinances.this, TransactionReadonly.class);
                transactionIntent.putExtra(TransactionReadonly.TRANSACTION_ID, transaction.getId());
                MyFinances.this.startActivity(transactionIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        new LoadTransactionsTask().execute();
    }

    private class LoadTransactionsTask extends AsyncTask<Void, Void, List<TransactionDto>> {

        @Override
        protected List<TransactionDto> doInBackground(Void... params) {
            DbAdapter dbAdapter = new DbAdapter(MyFinances.this);
            dbAdapter.open();
            List<TransactionDto> transactions = dbAdapter.getAllTransactions();
            dbAdapter.close();

            return transactions;
        }

        @Override
        protected void onPostExecute(List<TransactionDto> transactionDtos) {
            ArrayAdapter<TransactionDto> adapter = new ArrayAdapter<>(MyFinances.this, android.R.layout.simple_list_item_1, transactionDtos);
            ListView list = (ListView)findViewById(R.id.entrylist);
            list.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_finances, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this, PreferencesActivity.class);
            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void navigateToTransaction(View view) {
        Intent intent = new Intent(this, Transactions.class);
        this.startActivity(intent);
    }

    public void navigateToAccounts(View view) {
        Intent intent = new Intent(this, Accounts.class);
        this.startActivity(intent);
    }

    public void navigateToCategories(View view) {
        Intent intent = new Intent(this, Categories.class);
        this.startActivity(intent);
    }

    public void navigateToAnalyzer(View view)
    {
        Intent intent = new Intent(this, Analyzer.class);
        this.startActivity(intent);
    }
}
