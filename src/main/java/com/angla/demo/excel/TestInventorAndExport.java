package com.angla.demo.excel;

import com.angla.plugins.excel.ExcelFactory;
import com.angla.plugins.excel.commons.bean.InventorParseResult;
import com.angla.plugins.excel.commons.enums.CheckRuleEnum;
import com.angla.plugins.excel.export.Exporter;
import com.angla.plugins.excel.inventor.parse.Inventor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Title:TestInventorAndExport
 *
 * @author angla
 **/
public class TestInventorAndExport {


    public static void main(String[] args) {


        try {
            File fileIn = new File("####/test4.xls");
            Inventor<InventorBean> inventor = ExcelFactory.initInventor(fileIn, InventorBean.class,
                    CheckRuleEnum.CONTINUE_WHEN_ERROR);
            InventorParseResult inventorParseResult = inventor.parse();
            if (CollectionUtils.isNotEmpty(inventorParseResult.getErrList())) {
                Exporter<InventorBean> exporter = ExcelFactory.initExporter(inventorParseResult.getErrList());
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
