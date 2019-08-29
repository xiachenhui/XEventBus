package com.example.xeventbus.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/29/029 6:57
 * desc : 也可以通过注解来获取方法名
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodId {

    String value();
}
