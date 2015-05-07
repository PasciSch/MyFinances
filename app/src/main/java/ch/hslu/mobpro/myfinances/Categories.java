package ch.hslu.mobpro.myfinances;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.List;


public class Categories extends ListActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DbAdapter adapter = new DbAdapter(this);
        adapter.open();
        List<CategoryDto> categoryDtos = adapter.getCategories();
        adapter.close();
        ArrayAdapter<CategoryDto> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryDtos);
        this.setListAdapter(categoryAdapter);
    }
}
