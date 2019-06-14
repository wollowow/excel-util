package com.angla.demo.excel;

import com.angla.plugins.excel.ExcelFactory;
import com.angla.plugins.excel.commons.enums.CheckRuleEnum;
import com.angla.plugins.excel.export.Exporter;
import com.angla.plugins.excel.inventor.parse.Inventor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Title:TestInventorAndExport
 *
 * @author liumenghua
 **/
public class TestInventorAndExport {


    public static void main(String[] args) {

        InputStream fileIn = null;
        try {
            fileIn = new FileInputStream("/Users/menghualiu/Desktop/document/test/test2.xlsx");
            Inventor<InventorBean> inventor = ExcelFactory.initInventor(fileIn, InventorBean.class,
                    CheckRuleEnum.CONTINUE_WHEN_ERROR);
            List list = inventor.parse();
            if (CollectionUtils.isNotEmpty(list)) {
                Exporter<InventorBean> exporter = ExcelFactory.initExporter(list);
                Workbook workbook = exporter.generalExport();
                OutputStream outputStream = new FileOutputStream(new File("/Users/menghualiu/Desktop/test.xlsx"));
                workbook.write(outputStream);
                outputStream.flush();
                outputStream.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
