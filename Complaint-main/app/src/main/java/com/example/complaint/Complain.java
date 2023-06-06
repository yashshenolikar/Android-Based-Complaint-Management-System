package com.example.complaint;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Complain implements Parcelable {
    private String ticketNumber;
    private String deptName;
    private String location;
    private String complain;

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Complain(String deptName, String location, String complain) {
        this.deptName = deptName;
        this.location = location;
        this.complain = complain;
    }

    public Complain(){}

    public Complain(String ticketNumber, String deptName, String location, String complain) {
        this.ticketNumber = ticketNumber;
        this.deptName = deptName;
        this.location = location;
        this.complain = complain;
    }

    protected Complain(Parcel in) {
        deptName = in.readString();
        location = in.readString();
        complain = in.readString();
    }

    public static final Creator<Complain> CREATOR = new Creator<Complain>() {
        @Override
        public Complain createFromParcel(Parcel in) {
            return new Complain(in);
        }

        @Override
        public Complain[] newArray(int size) {
            return new Complain[size];
        }
    };

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComplain() {
        return complain;
    }

    public void setComplain(String complain) {
        this.complain = complain;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(deptName);
        dest.writeString(location);
        dest.writeString(complain);
    }
}
