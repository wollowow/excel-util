package com.angla.plugins.excel.export.anno;


import com.angla.plugins.excel.commons.enums.ISEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 导出字段注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExportField {

    String name() default "";  //导出标题名称

    String format() default ""; //格式化日期

    int scale() default 0;  //数字保留小数点

    String prefix() default ""; //导出数据前缀

    String suffix() default ""; //导出数据后缀

    boolean percent() default false; //百分比输出

    Class enumRule() default ISEnum.class; //注解数据处理

    Class custom() default CustomRule.class; //自定义规则

}
