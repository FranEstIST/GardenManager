package pt.ulisboa.tecnico.gardenmanager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.constants.ViewModes;
import pt.ulisboa.tecnico.gardenmanager.databinding.ActivityMainBinding;
import pt.ulisboa.tecnico.gardenmanager.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;

    private static final String TAG = "SearchActivity";

    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent receivedIntent = getIntent();
        mode = receivedIntent.getIntExtra("mode", -1);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String appBarTitle;
        String searchText;

        if(mode == ViewModes.DEVICE_MODE) {
            appBarTitle = getString(R.string.add_existing_device);
            searchText = getString(R.string.search_for_a_device_to_add_it);
        } else {
            appBarTitle = getString(R.string.add_existing_garden);
            searchText = getString(R.string.search_for_a_garden_to_add_it);
        }

        TextView appBarTitleTextView = binding.appBarTitle;
        TextView searchTextView = binding.searchTextView;

        appBarTitleTextView.setText(appBarTitle);
        searchTextView.setText(searchText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String queryHint;

        if(mode == ViewModes.DEVICE_MODE) {
            queryHint = getString(R.string.type_a_device_name_or_id);
        } else {
            queryHint = getString(R.string.type_a_garden_name_or_id);
        }

        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem menuSearchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuSearchItem.getActionView();
        searchView.setQueryHint(Html.fromHtml("<font color =" +
                getColor(R.color.medium_gray) +
                ">" +
                queryHint +
                "</font>"));

        //searchView.setBackgroundColor(getColor(R.color.black));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "Entered: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(SearchActivity.this.getBaseContext(), "Entered: " + newText, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Entered: " + newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}