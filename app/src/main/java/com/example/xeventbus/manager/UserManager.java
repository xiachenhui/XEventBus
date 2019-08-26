package com.example.xeventbus.manager;

import com.example.xeventbus.Friend;
import com.example.xeventbus.annotion.ClassId;

@ClassId("UserManager")
public class UserManager {
    private static UserManager userManager = null;
    private Friend friend;

    private UserManager() {
    }

    public static synchronized UserManager getInstance() {
        if (userManager == null) {
            synchronized (UserManager.class) {
                if (userManager == null) {
                    userManager = new UserManager();
                }
            }
        }
        return userManager;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public Friend getFriend() {
        return friend;
    }
}
