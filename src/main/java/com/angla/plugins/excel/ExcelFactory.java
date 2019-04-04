package com.angla.plugins.excel;

import com.angla.plugins.excel.commons.enums.ExcelTypeEnum;
import com.angla.plugins.excel.export.ExcelExporter;
import com.angla.plugins.excel.export.ExcelXExporter;
import com.angla.plugins.excel.export.Exporter;

import java.util.List;

/**
 * @program: excel-util
 * @description: 工厂类
 * @author: angla
 * @create: 2018-08-02 17:40
 * @Version 1.0
 **/
public class ExcelFactory {


    /**
     * 初始化导出工具
     *
     * @param data  导出数据
     * @return
     */
    private static<T> Exporter<T> initExporter(List<T> data , ExcelTypeEnum excelEnum,
                                         List<String> columns) {

        
        if (ExcelTypeEnum.EXCEL_XLS.equals(excelEnum)) {
            if (null != data) {
                return new ExcelExporter(data, columns);
            }
            //数据量超过xls文件格式最大值时用xlsx文件格式进行导出
            if (data.size() <= ExcelTypeEnum.EXCEL_XLS.getMaxSize()) {
                return new ExcelExporter(data, columns);
            }
        }
        return new ExcelXExporter(data, columns);
    }

    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return
     */
    public static<T> Exporter<T> initExporter(List<T> data) {
        return initExporter(data, null, null);
    }


    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return
     */
    public static<T> Exporter<T> initExporter(List<T> data,List<String> columns) {
        return initExporter(data, null, columns);
    }


    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return
     */
    public static<T> Exporter<T> initExporter(List<T> data, ExcelTypeEnum excelEnum) {
        return initExporter(data, excelEnum,null);
    }

}
