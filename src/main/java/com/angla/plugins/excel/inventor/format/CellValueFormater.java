package com.angla.plugins.excel.inventor.format;

import java.text.ParseException;

/**
 * @author liumenghua
 * @create 2019-05-17 17:55
 * @desc 属性转化
 **/
public interface CellValueFormater {

    Object formatValue(String value,String type) throws ParseException;
}
