package com.example.xeventbus;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/28/028 7:45
 * desc : 跨进程通讯是通过把对象序列化之后转为json数据，来传输
 **/
public class Response implements Parcelable {
    private String data;

    /**
     * 反序列化
     *
     * @param in
     */
    protected Response(Parcel in) {
        data = in.readString();
    }

    /**
     * 序列化
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Response> CREATOR = new Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };
}
