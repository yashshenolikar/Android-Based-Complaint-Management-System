package com.example.complaint;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ProfileInformation implements Parcelable {
    private String mobileNumber;
    private String userName;
    private String userAddress;
    private String userMail;
    private String userPassword;

    public ProfileInformation(){}

    public ProfileInformation(String userName, String userMail, String mobileNumber, String userAddress, String userPassword) {
        this.mobileNumber = mobileNumber;
        this.userName = userName;
        this.userAddress = userAddress;
        this.userMail = userMail;
        this.userPassword = userPassword;
    }

    protected ProfileInformation(Parcel in) {
        mobileNumber = in.readString();
        userName = in.readString();
        userAddress = in.readString();
        userMail = in.readString();
        userPassword = in.readString();
    }

    public static final Creator<ProfileInformation> CREATOR = new Creator<ProfileInformation>() {
        @Override
        public ProfileInformation createFromParcel(Parcel in) {
            return new ProfileInformation(in);
        }

        @Override
        public ProfileInformation[] newArray(int size) {
            return new ProfileInformation[size];
        }
    };

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(mobileNumber);
        dest.writeString(userName);
        dest.writeString(userAddress);
        dest.writeString(userMail);
        dest.writeString(userPassword);
    }
}
