package com.angla.plugins.excel.commons.throwable.exception;

import com.angla.plugins.excel.commons.throwable.ExcelException;

/**
 * @program: excel-util
 * @description: excel为空异常
 * @author: angla
 * @create: 2018-08-01 19:56
 * @Version 1.0
 **/
public class ExcelEmptyException extends ExcelException {
    public ExcelEmptyException(){
        super();
    }
    public ExcelEmptyException(String mess){
        super(mess);
    }
}
