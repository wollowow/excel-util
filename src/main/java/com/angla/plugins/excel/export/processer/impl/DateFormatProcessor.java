package com.angla.plugins.excel.export.processer.impl;

import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import com.angla.plugins.excel.export.anno.ExportFieldBean;
import com.angla.plugins.excel.export.processer.ExportAnnoProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间格式导出
 * Title:DateFormatProcessor
 *
 * @author angla
 **/
public class DateFormatProcessor implements ExportAnnoProcessor {

    public Object process(Object value, ExportFieldBean exportField) throws ParameterException {
        if(null == value){
            return null;
        }
        boolean flag = value instanceof Date;
        String defaultPattern = "yyyy-MM-dd HH:mm:ss";
        if(!flag){
            throw new ParameterException(exportField.getFieldName()+":转化值非时间格式！");
        }
        Date date = (Date) value;
        String pattern = exportField.getFormat();
        if(null == pattern || "".equals(pattern)){
            pattern = defaultPattern;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(date);
    }

}
