package ch.hslu.mobpro.myfinances;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


public class MyFinances extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_finances);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        List<TransactionDto> transactions = dbAdapter.getFirstTransaction();
        ArrayAdapter<TransactionDto> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactions);
        ListView list = (ListView)findViewById(R.id.entrylist);
        list.setAdapter(adapter);
        dbAdapter.close();
    }

    public void navigateToTransaction(View view) {
        Intent intent = new Intent(this, Transactions.class);
        this.startActivity(intent);
    }
}
