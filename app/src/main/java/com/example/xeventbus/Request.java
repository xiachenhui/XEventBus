package com.example.xeventbus;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/28/028 7:45
 * desc : 跨进程通讯是通过把对象序列化之后转为json数据，来传输
 **/
public class Request implements Parcelable {
    //请求的对象  RequestBean 对应的json字符串
    private String data;
    //    请求对象的类型
    private int type;

    public Request(String data, int type) {
        this.data = data;
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public int getType() {
        return type;
    }

    /**
     * 反序列化
     *
     * @param in
     */
    protected Request(Parcel in) {
        data = in.readString();
        type = in.readInt();
    }


    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 序列化
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
        dest.writeInt(type);
    }
}
