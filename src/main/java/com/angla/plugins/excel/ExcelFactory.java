package com.angla.plugins.excel;

import com.angla.plugins.excel.commons.enums.ExcelTypeEnum;
import com.angla.plugins.excel.export.ExcelExporter;
import com.angla.plugins.excel.export.ExcelXExporter;
import com.angla.plugins.excel.export.Exporter;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @program: excel-util
 * @description: 工厂类
 * @author: angla
 * @create: 2018-08-02 17:40
 * @Version 1.0
 **/
public class ExcelFactory<T> {


    /**
     * 初始化导出工具
     *
     * @param data  导出数据
     * @param model 导出模板(特殊导出时必传)
     * @return
     */
    private static Exporter initExporter(Object data, InputStream model, ExcelTypeEnum excelEnum,
                                         List<String> columns) {

        if (null != excelEnum && ExcelTypeEnum.EXCEL_2003.equals(excelEnum)) {
            boolean flag = data instanceof List;
            if (!flag) {
                return new ExcelExporter(data, model, columns);
            }
            List list = (List) data;
            //数据量超过xls文件格式最大值时用xlsx文件格式进行导出
            if (list.size() <= ExcelTypeEnum.EXCEL_2003.getMaxSize()) {
                return new ExcelExporter(data, model, columns);
            }
        }
        return new ExcelXExporter(data, model, columns);
    }

    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return
     */
    public static Exporter initExporter(List data) {
        return initExporter(data, null, null, null);
    }


    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return
     */
    public static Exporter initExporter(List data,List<String> columns) {
        return initExporter(data, null, null, columns);
    }


    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return
     */
    public static Exporter initExporter(List data, ExcelTypeEnum excelEnum) {
        return initExporter(data, null, excelEnum,null);
    }


    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return
     */
    public static Exporter initExporter(List data, ExcelTypeEnum excelEnum , List<String> columns) {
        return initExporter(data, null, excelEnum,columns);
    }
    /**
     * 初始化导出工具
     *
     * @param data  导出数据
     * @param model 导出模板
     * @return
     */
    public static Exporter initExporter(Map<String, Object> data, InputStream model) {
        return initExporter(data, model, null);
    }

    /**
     * 初始化导出工具
     *
     * @param data  导出数据
     * @param model 导出模板
     * @return
     */
    public static Exporter initExporter(Map<String, Object> data, InputStream model, ExcelTypeEnum
            excelEnum) {
        return initExporter(data, model, excelEnum);
    }
}
