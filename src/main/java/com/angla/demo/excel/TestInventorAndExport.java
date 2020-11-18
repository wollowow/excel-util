package com.angla.demo.excel;

import com.angla.plugins.excel.ExcelFactory;
import com.angla.plugins.excel.inventor.bean.InventorParseResult;
import com.angla.plugins.excel.commons.enums.CheckRuleEnum;
import com.angla.plugins.excel.export.Exporter;
import com.angla.plugins.excel.inventor.parse.Inventor;
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


        try (OutputStream outputStream = new FileOutputStream("/Users/a/Desktop/error.xlsx")){
            File fileIn = new File("/Users/a/Desktop/测试模板.xlsx");
            Inventor<InventorBean> inventor = ExcelFactory.initInventor(fileIn, InventorBean.class,
                    CheckRuleEnum.CONTINUE_WHEN_ERROR);
            InventorParseResult inventorParseResult = inventor.parse();

            Exporter exporter = ExcelFactory.initExporter(inventorParseResult.getErrList(),
                    inventorParseResult.getTitles());
            Workbook workbook = exporter.listExport();
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
