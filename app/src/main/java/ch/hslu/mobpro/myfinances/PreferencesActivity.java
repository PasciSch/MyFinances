package ch.hslu.mobpro.myfinances;

import android.app.Activity;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import java.util.List;

public class PreferencesActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager()
            .beginTransaction()
            .replace(android.R.id.content, new PreferenceInitializer())
        .commit();
    }

    public static final class PreferenceInitializer extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            SetSelectionItems();
        }

        private void SetSelectionItems() {
            DbAdapter adapter = new DbAdapter(getActivity());
            adapter.open();
            final List<AccountDto> accounts = adapter.getAccounts();
            final List<CategoryDto> categories = adapter.getCategories();
            adapter.close();

            final CharSequence[] categoryCharSequence = new String[categories.size()];
            final CharSequence[] categoryIdCharSequence = new String[categories.size()];
            for (int i = 0; i < categories.size(); i++) {
                categoryCharSequence[i] = categories.get(i).getName();
                categoryIdCharSequence[i] = String.format("%d", categories.get(i).getId());
            }

            final CharSequence[] accountCharSequence = new String[accounts.size()];
            final CharSequence[] accountIdCharSequence = new String[accounts.size()];
            for (int i = 0; i < accounts.size(); i++) {
                accountCharSequence[i] = accounts.get(i).getName();
                accountIdCharSequence[i] = String.format("%d", accounts.get(i).getId());
            }

            ListPreference categoryListPreference = (ListPreference)findPreference(getString(R.string.persistence_key_categoryPreference));
            categoryListPreference.setEntries(categoryCharSequence);
            categoryListPreference.setEntryValues(categoryIdCharSequence);

            ListPreference accountsListPreference = (ListPreference)findPreference(getString(R.string.persistence_key_accountPreference));
            accountsListPreference.setEntries(accountCharSequence);
            accountsListPreference.setEntryValues(accountIdCharSequence);
        }
    }
}
