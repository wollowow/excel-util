package com.angla.demo.excel;

import com.angla.plugins.excel.ExcelFactory;
import com.angla.plugins.excel.commons.enums.CheckRuleEnum;
import com.angla.plugins.excel.inventor.parse.Inventor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Title:Test1
 *
 * @author liumenghua
 **/
public class Test1 {

    public static void main(String[] args) {
        FileInputStream fileIn = null;
        try {
            Long start = System.currentTimeMillis();
            fileIn = new FileInputStream("/Users/menghualiu/Desktop/document/test/test1.xlsx");
            Inventor<Student2> inventor = ExcelFactory.initInventor(fileIn,Student2.class,
                    CheckRuleEnum.CONTINUE_WHEN_ERROR);
            List list = inventor.parse();
            Long end = System.currentTimeMillis();

            System.out.println("解析数据"+ list.size());
            System.out.println("共耗时:"+(end-start));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
