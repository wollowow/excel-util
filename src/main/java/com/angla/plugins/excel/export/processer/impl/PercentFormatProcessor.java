package com.angla.plugins.excel.export.processer.impl;

import com.angla.plugins.excel.export.processer.ExportAnnoProcessor;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import com.angla.plugins.excel.export.anno.ExportFieldBean;

import java.text.NumberFormat;

/**
 * 百分比格式化
 * Title:PercentFormatProcessor
 *
 * @author angla
 **/
public class PercentFormatProcessor implements ExportAnnoProcessor {

    public Object process(Object value, ExportFieldBean exportFieldBean) throws ParameterException {
        if(null == value){
            return value;
        }
        Number data;
        try {
            data = (Number) value;
        } catch (ClassCastException e) {
            throw new ParameterException(exportFieldBean.getFieldName() + ":类型非数字格式！");
        }
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        int scale = exportFieldBean.getScale();
        numberFormat.setMinimumFractionDigits(scale);
        return numberFormat.format(data);
    }
}
