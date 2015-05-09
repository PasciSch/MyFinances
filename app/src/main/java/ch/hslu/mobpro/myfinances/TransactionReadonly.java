package ch.hslu.mobpro.myfinances;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;


public class TransactionReadonly extends Activity {
    public static final String TRANSACTION_ID = "transactionId";
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        this.id = intent.getExtras().getLong(TRANSACTION_ID);

        setContentView(R.layout.activity_transaction_readonly);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(TRANSACTION_ID, this.id);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.id = savedInstanceState.getLong(TRANSACTION_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadTransactionTask().execute(this.id);
    }

    private class LoadTransactionTask extends AsyncTask<Long, Void, TransactionDto>
    {
        @Override
        protected TransactionDto doInBackground(Long... params) {
            DbAdapter adapter = new DbAdapter(TransactionReadonly.this);
            adapter.open();
            TransactionDto transaction = adapter.getTransactionById(params[0]);
            adapter.close();
            return transaction;
        }

        @Override
        protected void onPostExecute(TransactionDto transactionDto) {
            TextView dateView = (TextView)findViewById(R.id.transaction_readonly_date);
            TextView amountView = (TextView)findViewById(R.id.transaction_readonly_amount);
            TextView categoryView = (TextView)findViewById(R.id.transaction_readonly_category);
            TextView accountView = (TextView)findViewById(R.id.transaction_readonly_account);
            TextView descriptionView = (TextView)findViewById(R.id.transaction_readonly_description);

            SimpleDateFormat format = new SimpleDateFormat(TransactionDto.DateFormat);
            dateView.setText(format.format(transactionDto.getDate()));
            amountView.setText(String.format("%.2f", transactionDto.getAmount()));
            categoryView.setText(transactionDto.getCategory().toString());
            accountView.setText(transactionDto.getAccount().toString());
            descriptionView.setText(transactionDto.getDescription());
        }
    }

    public void navigateBack(View view) {
        this.finish();
    }

    public void deleteTransaction(View view) {
        new DeleteTransactionTask().execute(id);
    }

    private class DeleteTransactionTask extends AsyncTask<Long, Void, Void>{
        @Override
        protected Void doInBackground(Long... params) {
            DbAdapter adapter = new DbAdapter(TransactionReadonly.this);
            adapter.open();
            adapter.deleteTransactionWithId(params[0]);
            adapter.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TransactionReadonly.this.finish();
        }
    }
}
