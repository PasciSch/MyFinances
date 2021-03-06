package ch.hslu.mobpro.myfinances;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class Accounts extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listactivity_noselection);
        new LoadAccountsTask().execute();
    }

    private class LoadAccountsTask extends AsyncTask<Void, Void, List<ExtendedAccountUiItem>> {

        @Override
        protected List<ExtendedAccountUiItem> doInBackground(Void... params) {
            DbAdapter dbAdapter = new DbAdapter(Accounts.this);
            dbAdapter.open();
            List<AccountDto> accountDtos = dbAdapter.getAccounts();
            List<ExtendedAccountUiItem> uiAccounts = new ArrayList<>();
            for(AccountDto dto : accountDtos){
                uiAccounts.add(new ExtendedAccountUiItem(dto, dbAdapter));
            }
            dbAdapter.close();
            return uiAccounts;
        }

        @Override
        protected void onPostExecute(List<ExtendedAccountUiItem> uiAccounts)
        {
            ArrayAdapter<ExtendedAccountUiItem> accountAdapter = new ArrayAdapter<>(Accounts.this, android.R.layout.simple_list_item_1, uiAccounts);
            Accounts.this.setListAdapter(accountAdapter);
        }
    }

    private class ExtendedAccountUiItem
    {
        private AccountDto accountDto;
        private float money;

        ExtendedAccountUiItem(final AccountDto accountDto, DbAdapter dbAdapter) {
            this.accountDto = accountDto;

            this.setMoney(dbAdapter);
        }

        private void setMoney(DbAdapter dbAdapter) {
            List<TransactionDto> transactions = dbAdapter.getTransactionsOfAccount(this.accountDto.getId());
            float tempMoney = 0;

            for (TransactionDto transaction : transactions)
            {
                if (transaction.getCategory().isGain()) {
                    tempMoney += transaction.getAmount();
                }
                else {
                    tempMoney -= transaction.getAmount();
                }
            }

            money = tempMoney;
        }

        @Override
        public String toString() {
            return String.format("%s: %.2f", accountDto, money);
        }
    }
}
