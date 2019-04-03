package com.angla.plugins.excel.analysis.anno.interfaces;

import java.util.List;

/**
 * @program: excel-util
 * @description: 转换接口
 * @author: angla
 * @create: 2018-08-08 09:40
 * @Version 1.0
 **/
public interface FieldTranform {

    //字段转换 用于注入bean属性
    Object tranform(Object obj);
}
