package com.itheima.hchat.controller;

public class singleton {

    // 私有化成员变量
    private static singleton instance = null;

    // 私有构造方法
    private singleton() {}

    public static singleton getSingleton(){

        if(instance == null){
            synchronized (singleton.class){
                if(instance == null){
                    instance =  new singleton();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        singleton s1 = singleton.getSingleton();
        singleton s2 = singleton.getSingleton();
        if(s1 == s2){
            System.out.println("66666");
        }

    }
}
