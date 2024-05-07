package pt.ulisboa.tecnico.gardenmanager.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pt.ulisboa.tecnico.gardenmanager.GlobalClass;
import pt.ulisboa.tecnico.gardenmanager.db.GardenDatabase;
import pt.ulisboa.tecnico.gardenmanager.domain.Device;
import pt.ulisboa.tecnico.gardenmanager.domain.DeviceWithReadings;
import pt.ulisboa.tecnico.gardenmanager.domain.Reading;
import pt.ulisboa.tecnico.gardenmanager.network.WithoutNetService;

public class PollServerForMessagesService extends Service {
    private static final String TAG = "PollServerForMessagesService";

    private LocalBinder binder;

    private GlobalClass globalClass;
    private WithoutNetService withoutNetService;
    private GardenDatabase gardenDatabase;

    private Timer pollServerTimer;

    public PollServerForMessagesService() {
        /*globalClass = (GlobalClass) getApplicationContext();
        gardenDatabase = globalClass.getGardenDatabase();
        withoutNetService = new WithoutNetService(globalClass);
        binder = new LocalBinder();*/
    }

    @Override
    public void onCreate() {
        super.onCreate();

        globalClass = (GlobalClass) getApplicationContext();
        gardenDatabase = globalClass.getGardenDatabase();
        withoutNetService = new WithoutNetService(globalClass);
        binder = new LocalBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        startPollingServer();
        return binder;
    }

    public class LocalBinder extends Binder {
        public PollServerForMessagesService getService() {
            return PollServerForMessagesService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopPollingServer();
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //startService(intent)
        Log.d(TAG, "Started polling service");
        startPollingServer();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopPollingServer();
        super.onDestroy();
    }

    private void startPollingServer() {
        Consumer<List<DeviceWithReadings>> onSuccessConsumer = new Consumer<List<DeviceWithReadings>>() {
            @Override
            public void accept(List<DeviceWithReadings> deviceWithReadings) throws Throwable {
                pollServerForMessages(deviceWithReadings);
            }
        };

        TimerTask pollServerTimerTask = new TimerTask() {
            @Override
            public void run() {
                getAllDevicesAndReadings(onSuccessConsumer);
            }
        };

        pollServerTimer = new Timer("poll-server-timer");

        pollServerTimer.scheduleAtFixedRate(pollServerTimerTask, 0, 5000);

        pollServerTimerTask.run();
    }

    private void stopPollingServer() {
        if(pollServerTimer != null) {
            pollServerTimer.cancel();
            pollServerTimer.purge();
        }
    }

    private void getAllDevicesAndReadings(Consumer<List<DeviceWithReadings>> onSuccessConsumer) {
        // Step 1: Get the list of devices and their readings from the db
        gardenDatabase
                .deviceDao()
                .getAllDevicesWithReadings()
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(onSuccessConsumer);

        // Step 2: Get the the most recent readings from the central server

        // Step 3: Send ACKs to the central server
        //withoutNetService.getReadingsAfterTimestamp();
    }

    private void pollServerForMessages(List<DeviceWithReadings> devicesWithReadings) {
        WithoutNetService.WithoutNetServiceResponseListener getReadingsResponseListener = new WithoutNetService.WithoutNetServiceResponseListener() {
            @Override
            public void onResponse(Object response) {
                ArrayList<Reading> receivedReadings = (ArrayList<Reading>) response;
                Reading[] receivedReadingsArray = new Reading[receivedReadings.size()];
                receivedReadings.toArray(receivedReadingsArray);

                gardenDatabase.readingDao()
                        .insertAll(receivedReadingsArray)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(() -> Log.d(TAG, "Added readings from server"));

                WithoutNetService.WithoutNetServiceResponseListener removeReadingResponseListener = new WithoutNetService.WithoutNetServiceResponseListener() {
                    @Override
                    public void onResponse(Object response) {

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                };

                for(Reading reading : receivedReadings) {
                    Log.d(TAG, "Received reading: " + reading);
                    withoutNetService.removeReading(reading, removeReadingResponseListener);
                }
            }

            @Override
            public void onError(String errorMessage) {

            }
        };

        for(DeviceWithReadings deviceWithReadings : devicesWithReadings) {
            int senderId = deviceWithReadings.device.getDeviceId();
            long mostRecentMessageTimestamp = 0;

            if(deviceWithReadings.readings.size() > 0) {
                List<Reading> sortedReadings = deviceWithReadings.readings.stream()
                        .sorted((readingOne, readingTwo) -> (int) (readingTwo.getTimestamp() - readingOne.getTimestamp()))
                        .collect(Collectors.toList());
                mostRecentMessageTimestamp = sortedReadings.get(0).getTimestamp();
            }

            /*Log.d(TAG, "Attempting to fetch the most recent readings for "
                    + deviceWithReadings.device.getName()
                    + " after timestamp "
                    + mostRecentMessageTimestamp);*/

            withoutNetService.getReadings(senderId, getReadingsResponseListener);
        }
    }
}