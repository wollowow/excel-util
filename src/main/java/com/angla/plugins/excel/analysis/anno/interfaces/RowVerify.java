package com.angla.plugins.excel.analysis.anno.interfaces;

import com.angla.plugins.excel.analysis.external.ErrDetail;

import java.util.List;

/**
 * @program: excel-util
 * @description: 校验接口
 * @author: angla
 * @create: 2018-08-08 09:38
 * @Version 1.0
 **/
public interface RowVerify<T> {

    //在注解校验之前校验
    ErrDetail beforeVerify(List<Object> row);

    //在注解校验之后校验
    ErrDetail afterVerify(T t);

}
