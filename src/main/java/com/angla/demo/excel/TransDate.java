package com.angla.demo.excel;


import com.angla.plugins.excel.analysis.anno.interfaces.FieldTranform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: excel-util
 * @description: 时间字段
 * @author: angla
 * @create: 2018-08-08 09:41
 * @Version 1.0
 **/
public class TransDate implements FieldTranform {
    SimpleDateFormat sdf = new SimpleDateFormat();
    @Override
    public Date tranform(Object obj){
        try {
            return sdf.parse((String) obj);
        } catch (ParseException e) {
            return null;
        }

    }
}
