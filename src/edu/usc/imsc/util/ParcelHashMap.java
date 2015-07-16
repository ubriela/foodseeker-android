package edu.usc.imsc.util;

import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;
 
public class ParcelHashMap implements Parcelable {
    private HashMap map;
 
    public ParcelHashMap() {
        map = new HashMap();
    }
 
    public ParcelHashMap(Parcel in) {
        map = new HashMap();
        readFromParcel(in);
    }
 
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ParcelHashMap createFromParcel(Parcel in) {
            return new ParcelHashMap(in);
        }
 
        public ParcelHashMap[] newArray(int size) {
            return new ParcelHashMap[size];
        }
    };
 
    @Override
    public int describeContents() {
        return 0;
    }
 
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(map.size());
        for (Object s: map.keySet()) {
            dest.writeString((String)s);
            dest.writeString((String)map.get(s));
        }
    }
 
    public void readFromParcel(Parcel in) {
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            map.put(in.readString(), in.readString());
        }
    }
 
    public String get(String key) {
        return (String) map.get(key);
    }
 
    public void put(String key, String value) {
        map.put(key, value);
    }
}