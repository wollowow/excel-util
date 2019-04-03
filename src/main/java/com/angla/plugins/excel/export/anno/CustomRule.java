package com.angla.plugins.excel.export.anno;

/**
 * @program: excel-util
 * @description: 自定义规则
 * @author: angla
 * @create: 2018-08-08 09:40
 * @Version 1.0
 **/
public interface CustomRule<T> {
    String rule(T obj);
}
