package com.angla.plugins.excel.commons.enums;

public enum CheckRuleEnum {

    //校验出错之后直接抛异常
    BREAK_WHEN_ERROR(1),

    //校验出错之后跳过当前行
    BREAK_LINE_WHEN_ERROR(2),

    //校验出错之后跳过当前单元格
    BREAK_CELL_WHEN_ERROR(3);



    CheckRuleEnum(Integer code ) {
        this.code = code;
    }

    Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
