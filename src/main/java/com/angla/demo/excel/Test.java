package com.angla.demo.excel;

import com.angla.plugins.excel.ExcelFactory;
import com.angla.plugins.excel.export.ExcelExporter;
import com.angla.plugins.excel.export.Exporter;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Title:Test
 *
 * @author angla
 **/
public class Test<T> {
    public static void main(String s[]) throws Exception {

        List<ExportBean> exportBeans = new ArrayList<>();
        Date current = new Date();
        for (int i = 0; i < 100; i++) {

            ExportBean exportBean = new ExportBean();
            exportBean.setCreateTime(current);
            exportBean.setId(Long.parseLong(i+""));
            exportBean.setMoney(new BigDecimal("2.33"));
            exportBean.setName("张三");
            exportBeans.add(exportBean);
        }

        Exporter<ExportBean> exporter = ExcelFactory.initExporter(exportBeans);
        Workbook workbook = exporter.generalExport();
        OutputStream outputStream = new FileOutputStream(new File("/Users/menghualiu/Desktop/tes.xls"));

        workbook.write(outputStream);
        outputStream.close();
    }
}