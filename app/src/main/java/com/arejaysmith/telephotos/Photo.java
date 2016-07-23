package com.arejaysmith.telephotos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Urge_Smith on 7/21/16.
 */
public class Photo implements Parcelable {

    @SerializedName("albumId")
    private int albumId;
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;
    @SerializedName("thumbnailUrl")
    private String thumbnailUrl;

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }


    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel source) {
            Photo mPhoto = new Photo();
            mPhoto.albumId = source.readInt();
            mPhoto.id = source.readInt();
            mPhoto.title = source.readString();
            mPhoto.url = source.readString();
            mPhoto.thumbnailUrl = source.readString();

            return mPhoto;
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(albumId);
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeString(thumbnailUrl);
    }
}