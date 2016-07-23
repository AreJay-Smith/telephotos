package com.arejaysmith.telephotos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Urge_Smith on 7/21/16.
 */
public class Album implements Parcelable {

    private int userId;
    private int id;
    private String title;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static final Parcelable.Creator<Album> CREATOR = new Creator<Album>() {
        public Album createFromParcel(Parcel source) {
            Album mAlbum = new Album();
            mAlbum.id = source.readInt();
            mAlbum.title = source.readString();

            return mAlbum;
        }

        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(title);
    }
}

