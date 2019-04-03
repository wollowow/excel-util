package com.angla.plugins.excel.commons.throwable.exception;

import com.angla.plugins.excel.commons.throwable.ExcelException;

/**
 * @program: excel-util
 * @description: 参数异常
 * @author: angla
 * @create: 2018-08-03 15:09
 * @Version 1.0
 **/
public class ParameterException extends ExcelException {
    public ParameterException(){}

    public ParameterException(String mess){
        super(mess);
    }
}
