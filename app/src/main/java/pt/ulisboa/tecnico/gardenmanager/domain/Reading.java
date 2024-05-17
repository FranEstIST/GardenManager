package pt.ulisboa.tecnico.gardenmanager.domain;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

@Entity
public class Reading {

    @PrimaryKey(autoGenerate = true)
    private int readingId;

    @ColumnInfo(name = "sender-id")
    private int senderId;

    @ColumnInfo(name = "value")
    private long value;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "reading-type")
    private ReadingType readingType;

    public Reading(int senderId, long value, ReadingType readingType) {
        this.senderId = senderId;
        this.value = value;
        this.readingType = readingType;
    }

    @Ignore
    public Reading(long timestamp, int senderId, long value) {
        this.timestamp = timestamp;
        this.senderId = senderId;
        this.value = value;
    }

    public Reading(JSONObject jsonObject) throws JSONException, IllegalArgumentException{
        this.senderId = jsonObject.getInt("sender");

        String payload = jsonObject.getString("payload");
        byte[] payloadByteArray = Base64.getDecoder().decode(payload);

        this.value = byteArrayToIntRev(payloadByteArray);

        this.timestamp = jsonObject.getInt("timestamp");

        this.readingType = null;
    }

    private long byteArrayToLong(byte[] byteArray) {
        long longValue = 0;
        for (byte b : byteArray) {
            longValue = (longValue << 8) + (b & 0xFF);
        }
        return longValue;
    }

    int byteArrayToIntRev(byte[] byteArray) {
        int intValue = 0;

        for (int i = byteArray.length - 1; i >= 0 ; i--) {
            intValue = (intValue << 8) + (byteArray[i] & 0xFF);
        }
        return intValue;
    }

    @NonNull
    @Override
    public String toString() {
        return "Sender ID: "
                + senderId
                + ", Timestamp: "
                + timestamp
                + ", Value: "
                + value;
    }

    public int getReadingId() {
        return readingId;
    }

    public void setReadingId(int readingId) {
        this.readingId = readingId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ReadingType getReadingType() {
        return readingType;
    }

    public void setReadingType(ReadingType readingType) {
        this.readingType = readingType;
    }
}

