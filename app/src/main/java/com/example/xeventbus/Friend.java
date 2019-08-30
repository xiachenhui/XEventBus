package com.example.xeventbus;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/30/030 7:01
 * desc : 传递的对象
 **/
public class Friend {
    String name;
    String age;

    @Override
    public String toString() {
        return "Friend{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }

    public Friend(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
