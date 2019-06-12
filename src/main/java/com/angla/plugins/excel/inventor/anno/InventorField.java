package com.angla.plugins.excel.inventor.anno;


import com.angla.plugins.excel.commons.enums.ISEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: excel-util
 * @description: bean中添加的基本注解，想要解析成bean必须添加此注解
 * @author: angla
 * @create: 2018-08-02 15:11
 * @Version 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InventorField {
    String  name() default "";                              //中文名字

    boolean required() default false;                       //校验是否必填

    String regex() default "";                              //自定义正则校验

    String format() default "";                             //校验日期格式

    Class<? extends ISEnum> enumRule() default ISEnum.class; //注解数据处理


    Class<? extends CustomCheckRule> custom() default CustomCheckRule.class;             //自定义校验规则

}