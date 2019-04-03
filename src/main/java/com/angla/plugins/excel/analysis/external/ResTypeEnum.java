package com.angla.plugins.excel.analysis.external;

/**
 * @program: excel-util
 * @description: Res的类型
 * @author: angla
 * @create: 2018-08-03 17:45
 * @Version 1.0
 **/
public enum ResTypeEnum {
    FINISH(1, "已处理完成"),
    EXCEL_ERR(2, "excel解析错误"),
    PARAMETER_ERR(3, "参数错误"),
    TITLE_ERR(4, "excel表头验证未通过"),
    EXCEL_EMPTY_ERR(5, "excel为空"),
    EXCEL_MIN_ROW_ERR(7, "excel行数小于最小值"),
    EXCEL_MAX_ROW_ERR(8, "excel行数大于最大值"),
    EXCEL_MIN_COLUMN_ERR(9, "excel列数小于最小值"),
    EXCEL_MAX_COLUMN_ERR(10, "excel列数大于最大值");

    private int code;
    private String name;

    ResTypeEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
