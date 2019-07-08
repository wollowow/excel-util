package com.angla.plugins.excel.export;

import com.angla.plugins.excel.commons.enums.ExcelTypeEnum;
import com.angla.plugins.excel.commons.throwable.exception.ExcelEmptyException;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import com.angla.plugins.excel.export.anno.ExportAnnoManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;

/**
 * Title:ExcelXExporter
 *
 * @author angla
 **/
public class ExcelXExporter<T> extends AbstractExporter<T> {

    public ExcelXExporter(List<T> data, List<String> columns) {
        super(data, ExcelTypeEnum.EXCEL_XLSX, columns);
    }

    public Workbook generalExport() throws Exception {

        if (data == null) {
            throw new ParameterException("传入参数错误，需要List");
        }
        if (CollectionUtils.isEmpty(data)) {
            throw new ExcelEmptyException("导出数据为空！");
        }
        Class tClass = data.get(0).getClass();
        ExportAnnoManager exportAnnoManager = new ExportAnnoManager();
        exportAnnoManager.init(data, tClass, columns);
        List<String> titles = exportAnnoManager.getTitles();
        List<List<String>> datas = exportAnnoManager.getDatas();
        /**读取excel文档，超过100行写入硬盘防止内存溢出.*/
        Workbook workBook = new SXSSFWorkbook(100);
        //创建标题行
        return buildWorkbook(titles, datas, workBook);
    }
}
