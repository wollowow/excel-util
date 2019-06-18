package com.angla.demo.excel;

import com.angla.plugins.excel.ExcelFactory;
import com.angla.plugins.excel.commons.bean.InventorParseResult;
import com.angla.plugins.excel.commons.enums.CheckRuleEnum;
import com.angla.plugins.excel.inventor.parse.Inventor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Title:Test
 *
 * @author angla
 **/
public class Test {

    public static void main(String[] args) {
        FileInputStream fileIn = null;
        try {
            Long start = System.currentTimeMillis();
            fileIn = new FileInputStream("####/test4.xls");
            Inventor<InventorBean2> inventor = ExcelFactory.initInventor(fileIn, InventorBean2.class,
                    CheckRuleEnum.CONTINUE_WHEN_ERROR);
            InventorParseResult inventorParseResult = inventor.parse();
            Long end = System.currentTimeMillis();

            System.out.println("解析数据"+ inventorParseResult.getSucList().size());
            System.out.println("共耗时:"+(end-start));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
