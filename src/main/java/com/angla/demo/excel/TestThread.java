package com.angla.demo.excel;

import com.angla.plugins.excel.ExcelFactory;
import com.angla.plugins.excel.commons.enums.CheckRuleEnum;
import com.angla.plugins.excel.inventor.parse.Inventor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Title:TestThread
 *
 * @author liumenghua
 **/
public class TestThread implements Runnable{

    int i;

    public TestThread(int i) {
        this.i = i;
    }

    public void run() {
        FileInputStream fileIn = null;
        Thread current = Thread.currentThread();
        int num = i%3;
        try {
            fileIn = new FileInputStream("/Users/menghualiu/Deskto" +
                    "p/document/test/test"+num+".xlsx");
            Inventor<Student2> inventor = ExcelFactory.initInventor(fileIn,Student2.class,
                    CheckRuleEnum.CONTINUE_WHEN_ERROR);
            List list = inventor.parse();
            System.out.println(current.getName()+":"+list.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String s[]) throws Exception {
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(new TestThread(i));
            thread.start();
        }

    }
}
