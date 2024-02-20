package pt.ulisboa.tecnico.gardenmanager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.adapters.GardenListAdapter;
import pt.ulisboa.tecnico.gardenmanager.adapters.SearchListAdapter;
import pt.ulisboa.tecnico.gardenmanager.constants.ViewModes;
import pt.ulisboa.tecnico.gardenmanager.databinding.ActivityMainBinding;
import pt.ulisboa.tecnico.gardenmanager.databinding.ActivitySearchBinding;
import pt.ulisboa.tecnico.gardenmanager.domain.Device;
import pt.ulisboa.tecnico.gardenmanager.domain.Garden;
import pt.ulisboa.tecnico.gardenmanager.network.WithoutNetService;
import pt.ulisboa.tecnico.gardenmanager.network.dto.DeviceDto;
import pt.ulisboa.tecnico.gardenmanager.network.dto.GardenDto;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    private int mode;

    private ActivitySearchBinding binding;
    private TextView searchTextView;
    private RecyclerView searchItemsRecyclerView;
    private SearchListAdapter searchListAdapter;
    private String searchText;

    private GlobalClass globalClass;
    private WithoutNetService WNService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent receivedIntent = getIntent();
        mode = receivedIntent.getIntExtra("mode", -1);

        globalClass = (GlobalClass) getApplication().getApplicationContext();
        WNService = new WithoutNetService(globalClass);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String appBarTitle;

        if(mode == ViewModes.DEVICE_MODE) {
            appBarTitle = getString(R.string.add_existing_device);
            searchText = getString(R.string.search_for_a_device_to_add_it);
        } else {
            appBarTitle = getString(R.string.add_existing_garden);
            searchText = getString(R.string.search_for_a_garden_to_add_it);
        }

        TextView appBarTitleTextView = binding.appBarTitle;
        searchTextView = binding.searchTextView;

        appBarTitleTextView.setText(appBarTitle);
        searchTextView.setText(searchText);

        searchItemsRecyclerView = binding.searchItemsRecyclerView;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchItemsRecyclerView.setLayoutManager(layoutManager);

        searchListAdapter = new SearchListAdapter(mode, globalClass);

        // --------------------------------------------------------------------------
        // First (naive) solution: Let's download the whole list of networks/nodes from
        // the WN Server, and then search this list
        // TODO: Fill out the search adapter with the list of networks/devices
        // --------------------------------------------------------------------------
        // Second (pro) solution: Let's query the server for a list of networks/nodes
        // corresponding to the user input every time it changes
        // --------------------------------------------------------------------------
        // Third (even better) solution: Let's use the 1st solution for the nodes, since
        // There shouldn't be too many of them, and the 2nd for the networks
        // --------------------------------------------------------------------------

        binding.searchItemsRecyclerView.setAdapter(searchListAdapter);
    }

    private void updateSearchListVisibility() {
        if (searchListAdapter.getItemCount() == 0) {
            searchItemsRecyclerView.setVisibility(View.GONE);
            searchTextView.setVisibility(View.VISIBLE);
            searchTextView.setText(R.string.no_results_found);
        } else {
            searchItemsRecyclerView.setVisibility(View.VISIBLE);
            searchTextView.setVisibility(View.GONE);
        }
    }

    private void filterResults(String query) {
        if(query.equals("")) {
            searchItemsRecyclerView.setVisibility(View.GONE);
            searchTextView.setVisibility(View.VISIBLE);
            searchTextView.setText(searchText);
            return;
        }

        //searchListAdapter.getFilter().filter(query);

        if(mode == ViewModes.DEVICE_MODE) {
            WithoutNetService.WithoutNetServiceResponseListener responseListener = new WithoutNetService.WithoutNetServiceResponseListener() {
                @Override
                public void onResponse(Object response) {
                    List<DeviceDto> deviceDtos = (List<DeviceDto>) response;
                    List<Device> devices = deviceDtos
                            .stream()
                            .map(deviceDto -> {
                                return new Device(deviceDto);
                            })
                            .collect(Collectors.toList());

                    searchListAdapter.setFilteredDevices(devices);

                    updateSearchListVisibility();
                }

                @Override
                public void onError(String errorMessage) {
                    //Log.e(TAG, errorMessage);
                }
            };

            WNService.getAllDevicesInGardenContainingSubstring(globalClass.getCurrentGardenId(), query, responseListener);
        } else {
            // TODO: Implement the garden filtering part
            WithoutNetService.WithoutNetServiceResponseListener responseListener = new WithoutNetService.WithoutNetServiceResponseListener() {
                @Override
                public void onResponse(Object response) {
                    List<GardenDto> gardenDtos = (List<GardenDto>) response;
                    List<Garden> gardens = gardenDtos
                            .stream()
                            .map(gardenDto -> {
                                return new Garden(gardenDto);
                            })
                            .collect(Collectors.toList());

                    searchListAdapter.setFilteredGardens(gardens);

                    updateSearchListVisibility();
                }

                @Override
                public void onError(String errorMessage) {
                    //Log.e(TAG, errorMessage);
                }
            };

            WNService.getAllGardensContainingSubstring(query, responseListener);
        }
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "Entered: " + query);
                filterResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(SearchActivity.this.getBaseContext(), "Entered: " + newText, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Entered: " + newText);
                filterResults(newText);
                return true;
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