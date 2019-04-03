package com.angla.plugins.excel.analysis.external;

/**
 * @program: excel-util
 * @description: 此行数据的错误类型
 * @author: angla
 * @create: 2018-08-02 17:40
 * @Version 1.0
 **/
public enum ErrDetailTypeEnum {
    MUST(1, "必填字段为空"),
    REGEX(2, "正则校验错误"),
    UNI(3, "唯一性验证错误"),
    FORMAT(4, "类型转换错误"),
    USER_DEFINED(5, "自定义校验未通过"),
    USER_DEFINED_EXCEPTION(6, "自定义校验抛出异常");

    private int code;
    private String name;

    ErrDetailTypeEnum(int code, String name){
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
