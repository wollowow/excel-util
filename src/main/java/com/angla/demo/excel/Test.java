package com.angla.demo.excel;

import com.angla.plugins.excel.inventor.parse.ExcelX2Object;
import com.angla.plugins.excel.inventor.parse.Inventor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Title:Test
 *
 * @author angla
 **/
public class Test<T> {
    public static void main(String s[]) throws Exception {

        InputStream inputStream = new FileInputStream(new File("/Users/menghualiu/Desktop/test2.xlsx"));

        Inventor<Student> inventor = new ExcelX2Object<>(inputStream, Student.class);

        System.out.println(inventor.parse().size());

    }
}