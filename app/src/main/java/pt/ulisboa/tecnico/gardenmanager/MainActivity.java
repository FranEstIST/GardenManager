package pt.ulisboa.tecnico.gardenmanager;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import pt.ulisboa.tecnico.gardenmanager.databinding.ActivityMainBinding;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the database
        GardenDatabase gardenDatabase = Room
                .databaseBuilder(this.getApplicationContext(), GardenDatabase.class, "garden_database.db")
                .fallbackToDestructiveMigration()
                .build();

        GlobalClass globalClass = (GlobalClass) this.getApplicationContext();
        globalClass.setGardenDatabase(gardenDatabase);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawerLayout = binding.myDrawerLayout;
        NavigationView navView = binding.navView;

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration
                .Builder(navController.getGraph())
                .setOpenableLayout(drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}