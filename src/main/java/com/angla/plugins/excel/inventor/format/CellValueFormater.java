package com.angla.plugins.excel.inventor.format;

import java.text.ParseException;

/**
 * @author angla
 * @create 2019-05-17 17:55
 * @desc 属性转化
 **/
public interface CellValueFormater {

    /**
     * 根据bean类型转化属性
     * @param value  解析cell值
     * @param type   字段类型
     * @param format 时间格式
     * @return
     * @throws ParseException
     */
    Object formatValue(String value,String type,String format) throws ParseException;
}
