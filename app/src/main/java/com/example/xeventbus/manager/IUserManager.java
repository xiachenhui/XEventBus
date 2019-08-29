package com.example.xeventbus.manager;

import com.example.xeventbus.Friend;
import com.example.xeventbus.annotion.ClassId;

@ClassId("com.example.xeventbus.manager.UserManager")
public interface IUserManager {
    public Friend getFriend();
    public void setFriend(Friend friend);
}
