package com.angla.plugins.excel.commons.enums;


/**
 * 校验方式枚举
 */
public enum CheckRuleEnum {

    //校验出错之后直接抛异常
    BREAK_WHEN_ERROR(),


    //校验出错之后跳过当前单元格继续校验
    CONTINUE_WHEN_ERROR();


    CheckRuleEnum() {}
}
