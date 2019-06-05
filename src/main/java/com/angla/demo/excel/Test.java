package com.angla.demo.excel;

import com.angla.plugins.excel.ExcelFactory;
import com.angla.plugins.excel.inventor.parse.Inventor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Title:Test
 *
 * @author angla
 **/
public class Test<T> {
    public static void main(String s[]) throws Exception {

        InputStream inputStream = new BufferedInputStream(new FileInputStream("/Users/menghualiu/Desktop/test.xlsx"));

        Inventor<Student> inventor = ExcelFactory.initInventor(inputStream,Student.class);
        inventor.parse();

    }
}