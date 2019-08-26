package com.example.xeventbus;

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
