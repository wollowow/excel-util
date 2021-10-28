package com.angla.plugins.excel.export;

import com.angla.plugins.excel.commons.enums.ExcelTypeEnum;
import com.angla.plugins.excel.commons.throwable.exception.ExcelEmptyException;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import com.angla.plugins.excel.export.anno.ExportAnnoManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;

/**
 * 导出2003版本excel
 * Title:ExcelExporter
 *
 * @author angla
 **/
public class ExcelExporter<T> extends AbstractExporter<T> {


    public ExcelExporter(List<T> data, List<String> columns, boolean showErrMsg) {
        super(data, ExcelTypeEnum.EXCEL_XLS, columns, showErrMsg);
    }


    @Override
    public Workbook generalExport() throws Exception {
        return generalExport(null);
    }

    @Override
    public Workbook generalExport(Workbook workbook) throws Exception {
        if (null == workbook) {
            workbook = new HSSFWorkbook();
        }
        if (!(workbook instanceof HSSFWorkbook)) {
            throw new ParameterException("传入workbook类型错误");
        }
        if (null == data) {
            throw new ParameterException("传入参数错误");
        }
        List<T> list = data;
        if (CollectionUtils.isEmpty(list)) {
            throw new ExcelEmptyException("导出数据为空！");
        }
        ExportAnnoManager exportAnnoManager = new ExportAnnoManager(list, data.get(0).getClass(), columns, showErrMsg);
        return buildWorkbook(exportAnnoManager.getTitles(), exportAnnoManager.getDatas(), workbook);
    }

}
