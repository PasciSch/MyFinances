package ch.hslu.mobpro.myfinances;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import java.util.List;

public class Categories extends ListActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listactivity_noselection);
        new LoadCategoriesTask().execute();
    }

    private class LoadCategoriesTask extends AsyncTask<Void, Void, List<CategoryDto>> {

        @Override
        protected List<CategoryDto> doInBackground(Void... params) {
            DbAdapter dbAdapter = new DbAdapter(Categories.this);
            dbAdapter.open();
            List<CategoryDto> categoryDtos = dbAdapter.getCategories();
            dbAdapter.close();
            return categoryDtos;
        }

        @Override
        protected void onPostExecute(List<CategoryDto> categoryDtos)
        {
            ArrayAdapter<CategoryDto> categoryAdapter = new ArrayAdapter<>(Categories.this, android.R.layout.simple_list_item_1, categoryDtos);
            Categories.this.setListAdapter(categoryAdapter);
        }
    }
}
