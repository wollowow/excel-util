package com.angla.plugins.excel.commons.throwable.exception;

import com.angla.plugins.excel.commons.throwable.ExcelException;

/**
 * @program: excel-util
 * @description: 注解异常
 * @author: angla
 * @create: 2018-08-02 15:32
 * @Version 1.0
 **/
public class AnnotationException extends ExcelException {
    public AnnotationException(){
        super();
    }

    public AnnotationException(String mess){
        super(mess);
    }
}
