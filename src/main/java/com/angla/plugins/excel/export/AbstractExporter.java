package com.angla.plugins.excel.export;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.angla.plugins.excel.commons.enums.ExcelTypeEnum;
import com.angla.plugins.excel.commons.throwable.exception.ExcelEmptyException;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;

/**
 * 导出抽象类.
 * Title:AbstractExporter
 *
 * @author angla
 **/
public abstract class AbstractExporter<T> implements Exporter<T> {

    protected Object data;
    protected List<String> columns;
    private InputStream model;
    private ExcelTypeEnum excelEnum;

    AbstractExporter(Object data, InputStream model, ExcelTypeEnum excelEnum, List<String> columns) {
        this.data = data;
        this.model = model;
        this.excelEnum = excelEnum;
        this.columns = columns;
    }

    public abstract Workbook generalExport() throws Exception;

    public boolean modelExport() {
        // TODO: 2018/8/9 模板导出待开发
        System.out.println("模板导出");
        return false;
    }

    public Workbook listExport() throws Exception {
        boolean flag = data instanceof List;
        if (!flag) {
            throw new ParameterException("传入参数错误，需要List");
        }
        List<List<Object>> list = (List) data;
        if (CollectionUtils.isEmpty(list)) {
            throw new ExcelEmptyException("导出数据为空！");
        }
        flag = list.get(0) instanceof List;
        if (!flag) {
            throw new ParameterException("传入参数错误，需要List<List<Object>>类型数据");
        }
        boolean isXlsx = true;
        if(null != excelEnum && ExcelTypeEnum.EXCEL_2003.equals(excelEnum)){
            isXlsx = false;
        }
        ExcelWriter writer = ExcelUtil.getWriter(isXlsx);
        if (!CollectionUtils.isEmpty(columns)) {
            writer.write(columns);
        }
        for (List<Object> re : list) {
            writer.write(re);
        }
        return writer.getWorkbook();
    }

    public ExcelTypeEnum getExcelEnum() {
        return this.excelEnum;
    }
}
