package pt.ulisboa.tecnico.gardenmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.databinding.ActivityAddNewGardenPopUpBinding;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;
import pt.ulisboa.tecnico.gardenmanager.domain.Garden;

public class AddNewGardenPopUpActivity extends AppCompatActivity {
    public static final String TAG = "AddNewGardenPopUpActivity";
    public ActivityAddNewGardenPopUpBinding binding;
    private GlobalClass globalClass;

    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewGardenPopUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        this.getWindow().setLayout((int) (dm.widthPixels * 0.5), (int) (dm.heightPixels * 0.5));

        TextView editGardenIdTextView = binding.editGardenIdTextView;
        TextView editGardenNameTextView = binding.editGardenNameTextView;
        Button submitButton = binding.submitButton;

        this.globalClass = (GlobalClass) this.getApplicationContext();
        GardenDatabase gardenDatabase = this.globalClass.getGardenDatabase();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gardenId = editGardenIdTextView.getText().toString();
                int gardenIdInt;

                try {
                    gardenIdInt = Integer.parseInt(gardenId);
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Invalid garden Id");
                    return;
                }

                String gardenName = editGardenNameTextView.getText().toString();

                Garden newGarden = new Garden(gardenIdInt, gardenName);

                gardenDatabase.gardenDao().insertAll(newGarden)
                        .observeOn(Schedulers.newThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "Added garden");
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                e.printStackTrace();
                            }
                        });
            }
        });
    }
}