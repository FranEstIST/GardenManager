package pt.ulisboa.tecnico.gardenmanager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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

import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.R;
import pt.ulisboa.tecnico.gardenmanager.adapters.GardenListAdapter;
import pt.ulisboa.tecnico.gardenmanager.adapters.SearchListAdapter;
import pt.ulisboa.tecnico.gardenmanager.constants.ViewModes;
import pt.ulisboa.tecnico.gardenmanager.databinding.ActivityMainBinding;
import pt.ulisboa.tecnico.gardenmanager.databinding.ActivitySearchBinding;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;
import pt.ulisboa.tecnico.gardenmanager.domain.Device;
import pt.ulisboa.tecnico.gardenmanager.domain.Garden;
import pt.ulisboa.tecnico.gardenmanager.domain.GardenWithDevices;
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

        searchItemsRecyclerView = binding.searchItemsRecyclerView;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchItemsRecyclerView.setLayoutManager(layoutManager);

        SearchListAdapter.SearchListItemOnClickListener searchListItemOnClickListener = new SearchListAdapter.SearchListItemOnClickListener() {
            @Override
            public void onClick(int clickedPosition) {
                GardenDatabase gardenDatabase = globalClass.getGardenDatabase();

                if(mode == ViewModes.DEVICE_MODE) {
                    Device clickedDevice = searchListAdapter.getFilteredDevices().get(clickedPosition);

                    gardenDatabase.deviceDao().insertAll(clickedDevice)
                            .observeOn(Schedulers.newThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new DisposableCompletableObserver() {
                                @Override
                                public void onComplete() {
                                    Log.d(TAG, "Added device");
                                    SearchActivity.this.finish();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }
                            });

                    // TODO: Test this

                } else {
                    Garden clickedGarden = searchListAdapter.getFilteredGardens().get(clickedPosition);

                    gardenDatabase.gardenDao().insertAll(clickedGarden)
                            .observeOn(Schedulers.newThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new DisposableCompletableObserver() {
                                @Override
                                public void onComplete() {
                                    Log.d(TAG, "Added garden");
                                    SearchActivity.this.finish();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }
                            });

                    // TODO: Test this
                }
            }
        };

        searchListAdapter = new SearchListAdapter(mode, searchListItemOnClickListener);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String appBarTitle;

        if(mode == ViewModes.DEVICE_MODE) {
            appBarTitle = getString(R.string.add_existing_device);
            searchText = getString(R.string.search_for_a_device_to_add_it);
            //getAllDevicesInGarden();
        } else {
            appBarTitle = getString(R.string.add_existing_garden);
            searchText = getString(R.string.search_for_a_garden_to_add_it);
        }

        TextView appBarTitleTextView = binding.appBarTitle;
        searchTextView = binding.searchTextView;

        appBarTitleTextView.setText(appBarTitle);
        searchTextView.setText(searchText);

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

    private void getAllDevicesInGarden() {
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

            }
        };

        WNService.getAllDevicesInGarden(globalClass.getCurrentGardenId(), responseListener);
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
            List<Garden> existingGardens = new ArrayList<>();

            // TODO: Implement the garden filtering part
            WithoutNetService.WithoutNetServiceResponseListener responseListener = new WithoutNetService.WithoutNetServiceResponseListener() {
                @Override
                public void onResponse(Object response) {
                    List<GardenDto> gardenDtos = (List<GardenDto>) response;
                    List<Garden> filteredGardens = gardenDtos
                            .stream()
                            .map(gardenDto -> {
                                return new Garden(gardenDto);
                            })
                            .collect(Collectors.toList());

                    // Remove gardens that are already in the db from the list of filtered gardens
                    filteredGardens = filteredGardens
                            .stream()
                            .filter(gardenOne -> {
                                return !(existingGardens
                                    .stream()
                                    .anyMatch(gardenTwo -> {
                                        return gardenOne.getGardenId() == gardenTwo.getGardenId();
                                    }));
                            }).collect(Collectors.toList());

                    searchListAdapter.setFilteredGardens(filteredGardens);

                    updateSearchListVisibility();
                }

                @Override
                public void onError(String errorMessage) {
                    //Log.e(TAG, errorMessage);
                }
            };

            globalClass.getGardenDatabase()
                    .gardenDao()
                    .getAll()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(Schedulers.io())
                    .subscribe(new DisposableSingleObserver<List<Garden>>() {
                        @Override
                        public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<Garden> gardens) {
                            existingGardens.addAll(gardens);
                            WNService.getAllGardensContainingSubstring(query, responseListener);
                        }

                        @Override
                        public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                            Log.d(TAG, "Error getting existing gardens.", e);
                        }
                    });
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