package com.angla.demo.excel;

import com.angla.plugins.excel.inventor.parse.ExcelX2Object;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

/**
 * Title:InventorThread
 *
 * @author liumenghua
 **/
public class InventorThread extends Thread {

    private String filePath;

    public InventorThread(String filePath) {
        this.filePath = filePath;
    }

    public void run() {

        Long start = System.currentTimeMillis();
        File xlsxFile = new File(filePath);
        if (!xlsxFile.exists()) {
            System.err.println("Not found or not a file: " + xlsxFile.getPath());
            return;
        }
        try {
            ExcelX2Object<Student> xlsx2csv = new ExcelX2Object<>(filePath, Student.class);
            xlsx2csv.parse();
            Thread thread = currentThread();
            System.out.println(thread.getName()+"解析完成");
            Long end = System.currentTimeMillis();
            System.out.println(thread.getName()+"共耗时:" + (end - start));
        } catch (OpenXML4JException | SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
