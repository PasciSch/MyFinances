package ch.hslu.mobpro.myfinances;

import android.app.Activity;
import android.os.Bundle;
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

        EditText dateEditText = (EditText)findViewById(R.id.entry_date);
        SimpleDateFormat format = new SimpleDateFormat(TransactionDto.DateFormat);
        dateEditText.setText(format.format(new Date()));

        DbAdapter adapter = new DbAdapter(this);
        adapter.open();
        List<AccountDto> accountDtos = adapter.getAccounts();
        List<CategoryDto> categoryDtos = adapter.getCategories();
        adapter.close();

        ArrayAdapter<AccountDto> accountAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accountDtos);
        Spinner accountSpinner = (Spinner)findViewById(R.id.entry_account);
        accountSpinner.setAdapter(accountAdapter);

        ArrayAdapter<CategoryDto> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryDtos);
        Spinner categorySpinner = (Spinner)findViewById(R.id.entry_categorie);
        categorySpinner.setAdapter(categoryAdapter);

        EditText amountEditText = (EditText)findViewById(R.id.entry_amount);
        amountEditText.setText("0");
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

            DbAdapter adapter = new DbAdapter(this);
            adapter.open();
            adapter.insertTransaction(transaction);
            adapter.close();

            Toast.makeText(this, String.format("%f", transaction.getId()), Toast.LENGTH_SHORT).show();

            this.finish();
        }
        catch (ParseException exception)
        {
            Toast.makeText(Transactions.this, "Invalid Date", Toast.LENGTH_SHORT).show();
        }
        catch (NumberFormatException exception)
        {
            Toast.makeText(Transactions.this, "Invalid Amount", Toast.LENGTH_SHORT).show();
        }
    }
}
