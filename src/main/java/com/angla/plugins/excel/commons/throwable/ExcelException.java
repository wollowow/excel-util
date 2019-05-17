package com.angla.plugins.excel.commons.throwable;

/**
 * @program: excel-util
 * @description: excel异常
 * @author: angla
 * @create: 2018-08-03 15:08
 * @Version 1.0
 **/
public class ExcelException extends RuntimeException {
    public ExcelException(){}

    public ExcelException(String mess){
        super(mess);
    }

    public ExcelException(String s, Throwable cause) {
        super(cause);
    }
}
