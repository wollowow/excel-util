package com.angla.demo.excel;

import com.angla.plugins.excel.ExcelFactory;
import com.angla.plugins.excel.export.Exporter;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Title:TestExport
 *
 * @author angla
 **/
public class TestExport {

    public static void main(String[] args) {
        List<List<String>> data = new ArrayList<>();

        List<String> cloumn = new ArrayList<>();
        cloumn.add("姓名");
        cloumn.add("年龄");
        cloumn.add("性别");
        cloumn.add("哈哈");
        cloumn.add("啦啦");
        for (int i = 0; i < 10000; i++) {
            List<String> row = new ArrayList<>();
            row.add("张三" + i);
            row.add(i + "");
            row.add("男");
            row.add("哈哈哈");
            row.add("哈哈哈");
            row.add("哈哈哈");
            row.add("哈哈哈");
            row.add("哈哈哈");
            row.add("哈哈哈");
            row.add("哈哈哈");
            row.add("哈哈哈");
            row.add("哈哈哈");

            data.add(row);
        }
        Exporter<List<String>> exporter = ExcelFactory.initExporter(data,cloumn);
        try (OutputStream outputStream =
                     new FileOutputStream(new File("/Users/menghualiu/Desktop/test"+exporter.getExcelEnum().getSuffix()));
             Workbook workbook = exporter.listExport()) {
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
