package com.angla.plugins.excel.export;

import com.angla.plugins.excel.commons.enums.ExcelTypeEnum;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author angla
 * @create 2018-08-09 上午9:46
 * @desc 导出类接口
 **/
public interface Exporter<T> {

    /**
     * 对象数据普通导出
     *
     * @return
     */
    Workbook generalExport() throws Exception;

    /**
     * 直接导出list数据
     *
     * @return
     * @throws Exception
     */
    Workbook listExport() throws Exception;

    /**
     * 带模板导出
     *
     * @return
     */
    boolean modelExport();

    /**
     * 获取excel信息
     *
     * @return
     */
    ExcelTypeEnum getExcelEnum();
}
