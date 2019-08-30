package com.example.xeventbus.manager;

import com.example.xeventbus.Friend;
import com.example.xeventbus.annotion.ClassId;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/30/030 7:06
 * desc : 定义接口，创建代理对象替代UserManager，因为UserManager是在服务端生成的，客户端拿不到
 **/
@ClassId("com.example.xeventbus.manager.UserManager")
public interface IUserManager {
    public Friend getFriend();

    public void setFriend(Friend friend);
}
