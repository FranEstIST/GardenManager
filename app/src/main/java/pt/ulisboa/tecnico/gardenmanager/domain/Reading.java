package pt.ulisboa.tecnico.gardenmanager.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

