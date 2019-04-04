package com.angla.plugins.excel.export;

import com.angla.plugins.excel.commons.enums.ExcelTypeEnum;
import com.angla.plugins.excel.commons.throwable.exception.ExcelEmptyException;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import com.angla.plugins.excel.export.anno.ExportAnnoManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * 导出2003版本excel
 * Title:ExcelExporter
 *
 * @author angla
 **/
public class ExcelExporter<T> extends AbstractExporter<T> {


    public ExcelExporter(List<T> data, List<String> columns) {
        super(data, ExcelTypeEnum.EXCEL_XLS, columns);
    }

    public Workbook generalExport() throws Exception {
        if (null == data) {
            throw new ParameterException("传入参数错误");
        }
        List<T> list =  data;
        if (CollectionUtils.isEmpty(list)) {
            throw new ExcelEmptyException("导出数据为空！");
        }
        Class tClass = data.get(0).getClass();
        ExportAnnoManager exportAnnoManager = new ExportAnnoManager();
        exportAnnoManager.init(list, tClass, columns);
        List<String> titles = exportAnnoManager.getTitles();
        List<List<String>> datas = exportAnnoManager.getDatas();
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet();
        //创建标题行
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.size(); i++) {
            Cell titleCell = titleRow.createCell(i);
            titleCell.setCellValue(titles.get(i));
        }
        for (int i = 0; i < datas.size(); i++) {
            List<String> data = datas.get(i);
            //从第二行开始写入数据
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < data.size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(data.get(j));
            }
        }
        return workBook;
    }

}
