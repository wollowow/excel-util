package com.angla.demo.excel;

import java.lang.reflect.ParameterizedType;

/**
 * Title:Test
 *
 * @author angla
 **/
public class Test<T> {
    public Class<T> getTClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void test() {
        System.out.println(getTClass());
    }

    public static void main(String s[]) {
        (new Test<ExportBean>() {}).test(); // class java.lang.String
    }
}