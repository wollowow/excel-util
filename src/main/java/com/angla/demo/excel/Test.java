package com.angla.demo.excel;

import com.angla.plugins.excel.ExcelFactory;
import com.angla.plugins.excel.commons.enums.CheckRuleEnum;
import com.angla.plugins.excel.inventor.parse.Inventor;

import java.io.FileInputStream;
import java.util.List;

/**
 * Title:Test
 *
 * @author angla
 **/
public class Test<T> {
    public static void main(String s[]) throws Exception {

        FileInputStream fileIn = new FileInputStream("/Users/menghualiu/Desktop/test.xlsx");

        Inventor<Student> inventor = ExcelFactory.initInventor(fileIn,Student.class, CheckRuleEnum.CONTINUE_WHEN_ERROR);
        List list = inventor.parse();
        System.out.println(list.size());

    }
}