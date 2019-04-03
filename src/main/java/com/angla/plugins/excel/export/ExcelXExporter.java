package com.angla.plugins.excel.export;

import com.angla.plugins.excel.commons.enums.ExcelTypeEnum;
import com.angla.plugins.excel.commons.throwable.exception.ExcelEmptyException;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import com.angla.plugins.excel.export.anno.ExportAnnoManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.InputStream;
import java.util.List;

/**
 * Title:ExcelXExporter
 *
 * @author angla
 **/
public class ExcelXExporter extends AbstractExporter {

    public ExcelXExporter(Object data, InputStream model, List<String> columns) {
        super(data, model, ExcelTypeEnum.EXCEL_2007, columns);
    }
    public Workbook generalExport() throws Exception {
        boolean flag = data instanceof List;
        if (!flag) {
            throw new ParameterException("传入参数错误，需要List");
        }
        List list = (List) data;
        if (CollectionUtils.isEmpty(list)) {
            throw new ExcelEmptyException("导出数据为空！");
        }
        Class tClass = list.get(0).getClass();
        ExportAnnoManager exportAnnoManager = new ExportAnnoManager();
        exportAnnoManager.init(list, tClass, columns);
        List<String> titles = exportAnnoManager.getTitles();
        List<List<String>> datas = exportAnnoManager.getDatas();
        /**读取excel文档，超过100行写入硬盘防止内存溢出.*/
        Workbook workBook = new SXSSFWorkbook(100);
        // sheet 对应一个工作页
        Sheet sheet = workBook.createSheet();
        //创建标题行
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.size(); i++) {
            Cell titleCell = titleRow.createCell(i);
            titleCell.setCellValue(titles.get(i));
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

    public boolean modelExport() {
        return super.modelExport();
    }
}
