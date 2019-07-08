package com.angla.plugins.excel.export;

import com.angla.plugins.excel.commons.enums.ExcelTypeEnum;
import com.angla.plugins.excel.commons.throwable.ExcelException;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;

/**
 * 导出抽象类.
 * Title:AbstractExporter
 *
 * @author angla
 **/
public abstract class AbstractExporter<T> implements Exporter<T> {

    List<T> data;
    List<String> columns;
    private ExcelTypeEnum excelEnum;

    AbstractExporter(List<T> data, ExcelTypeEnum excelEnum, List<String> columns) {
        this.data = data;
        this.excelEnum = excelEnum;
        this.columns = columns;
    }

    public abstract Workbook generalExport() throws Exception;

    @Override
    public Workbook listExport() {
        boolean flag = data != null;
        if (!flag) {
            throw new ParameterException("传入参数错误，需要List");
        }
        List<List<String>> list = (List<List<String>>) data;
        if (CollectionUtils.isEmpty(list)) {
            throw new ExcelException("数据类型错误或导出数据为空！");
        }
        Workbook workbook;
        if (ExcelTypeEnum.EXCEL_XLS.equals(excelEnum) && list.size() < ExcelTypeEnum.EXCEL_XLS.getMaxSize()) {
            workbook = new HSSFWorkbook();
        } else {
            workbook = new SXSSFWorkbook(100);
        }
        return buildWorkbook(columns, list, workbook);
    }

    @Override
    public ExcelTypeEnum getExcelEnum() {
        return this.excelEnum;
    }


    Workbook buildWorkbook(List<String> titles, List<List<String>> datas, Workbook workBook) {
        Sheet sheet = workBook.createSheet();
        Row titleRow = sheet.createRow(0);
        if(CollectionUtils.isNotEmpty(titles)){
            for (int i = 0; i < titles.size(); i++) {
                Cell titleCell = titleRow.createCell(i);
                titleCell.setCellValue(titles.get(i));
            }
        }
        for (int i = 0; i < datas.size(); i++) {
            List<String> data = datas.get(i);
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < data.size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(data.get(j));
            }
        }
        return workBook;
    }

}
