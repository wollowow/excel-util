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

    public ExcelXExporter(List<T> data, List<String> columns, boolean showErrMsg) {
        super(data, ExcelTypeEnum.EXCEL_XLSX, columns, showErrMsg);
    }

    public Workbook generalExport() throws Exception {
        return generalExport(null);
    }

    public Workbook generalExport(Workbook workbook) throws Exception {
        if (null == workbook) {
            workbook = new SXSSFWorkbook(100);
        }
        if (!(workbook instanceof SXSSFWorkbook)) {
            throw new ParameterException("传入workbook类型错误");
        }
        if (data == null || CollectionUtils.isEmpty(data)) {
            throw new ExcelEmptyException("导出数据为空！");
        }
        ExportAnnoManager exportAnnoManager = new ExportAnnoManager(data, data.get(0).getClass(), columns, showErrMsg);
        //读取excel文档，超过100行写入硬盘防止内存溢出
        //创建标题行
        return buildWorkbook(exportAnnoManager.getTitles(), exportAnnoManager.getDatas(), workbook);
    }
}
