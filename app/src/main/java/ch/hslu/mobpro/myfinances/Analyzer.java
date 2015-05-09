package ch.hslu.mobpro.myfinances;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class Analyzer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyzer);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.SetInitialDateValues();

        this.RegisterDateChanges();

        new LoadTransactionsTask().execute();
    }

    private void RegisterDateChanges() {
        EditText startDateEditText = (EditText)findViewById(R.id.analyzer_start_date);
        startDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    new LoadTransactionsTask().execute();
                }
            }
        });

        EditText endDateEditText = (EditText)findViewById(R.id.analyzer_end_date);
        endDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    new LoadTransactionsTask().execute();
                }
            }
        });
    }

    private void SetInitialDateValues() {
        SimpleDateFormat format = new SimpleDateFormat(TransactionDto.DateFormat);

        EditText startDateEditText = (EditText)findViewById(R.id.analyzer_start_date);
        Calendar calendarMonthAgo = Calendar.getInstance();
        calendarMonthAgo.add(Calendar.MONTH, -1);
        java.util.Date startDate = calendarMonthAgo.getTime();
        startDateEditText.setText(format.format(startDate));

        Calendar calendarNow = Calendar.getInstance();
        java.util.Date todayDate = calendarNow.getTime();
        EditText endDateEditText = (EditText)findViewById(R.id.analyzer_end_date);
        endDateEditText.setText(format.format(todayDate));
    }

    private class LoadTransactionsTask extends AsyncTask<Void, Void, Void> {

        List<TransactionDto> transactionDtos;
        List<CategoryDto> categoryDtos;

        @Override
        protected Void doInBackground(Void... params) {
            DbAdapter dbAdapter = new DbAdapter(Analyzer.this);
            dbAdapter.open();
            transactionDtos = dbAdapter.getAllTransactions();
            categoryDtos = dbAdapter.getCategories();
            dbAdapter.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            SimpleDateFormat format = new SimpleDateFormat(TransactionDto.DateFormat);
            EditText startDateEditText = (EditText)findViewById(R.id.analyzer_start_date);
            EditText endDateEditText = (EditText)findViewById(R.id.analyzer_end_date);
            Date startDate;
            Date endDate;
            try {
                startDate = new Date(format.parse(startDateEditText.getText().toString()).getTime());
                endDate = new  Date(format.parse(endDateEditText.getText().toString()).getTime());
            } catch (ParseException e) {
                Toast.makeText(Analyzer.this, "Datum invalid", Toast.LENGTH_SHORT);
                return;
            }

            this.CalculateAndShowSum(startDate, endDate);
            this.CalculateAndShowCategories(startDate, endDate);

        }

        private void CalculateAndShowCategories(Date startDate, Date endDate) {
            List<String> categorySumStrings = new ArrayList<>();

            for (CategoryDto category : this.categoryDtos)
            {
                float categorySum = 0;

                for (TransactionDto transaction : this.transactionDtos)
                {
                    if (transaction.getCategory().getId() == category.getId())
                    {
                        Long transactionDate = transaction.getDate().getTime();
                        if (transactionDate >= startDate.getTime() && transactionDate <= endDate.getTime())
                        {
                            categorySum += transaction.getAmount();
                        }
                    }
                }

                categorySumStrings.add(String.format("%s Fr: %.2f", category.toString(), categorySum));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(Analyzer.this, android.R.layout.simple_list_item_1, categorySumStrings);
            ListView list = (ListView)findViewById(R.id.analyzer_category_list);
            list.setAdapter(adapter);
        }

        private void CalculateAndShowSum(Date startDate, Date endDate) {
            float income = 0;
            float expenses = 0;

            for (TransactionDto transaction : this.transactionDtos)
            {
                Long transactionDate = transaction.getDate().getTime();
                if (transactionDate >= startDate.getTime() && transactionDate <= endDate.getTime())
                {
                    if (transaction.getCategory().isGain()) {
                        income += transaction.getAmount();
                    } else {
                        expenses += transaction.getAmount();
                    }
                }

                TextView incomeText = (TextView)findViewById(R.id.analyzer_income);
                TextView expensesText = (TextView)findViewById(R.id.analyzer_expenses);
                TextView totalText = (TextView)findViewById(R.id.analyzer_total);

                incomeText.setText(String.format("%.2f", income));
                expensesText.setText(String.format("%.2f", expenses));
                totalText.setText(String.format("%.2f", income-expenses));
            }
        }
    }
}