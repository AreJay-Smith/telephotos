package com.arejaysmith.telephotos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Urge_Smith on 7/20/16.
 */
public class User implements Parcelable {

    private int id;
    private String name;

    public void User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            User mUser = new User();
            mUser.id = source.readInt();
            mUser.name = source.readString();

            return mUser;
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
    }
}
