package com.angla.plugins.excel.export.processer.impl;

import com.angla.plugins.excel.export.processer.ExportAnnoProcessor;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import com.angla.plugins.excel.export.anno.ExportFieldBean;

import java.math.BigDecimal;

/**
 * 时间格式导出
 * Title:DateFormatProcessor
 *
 * @author angla
 **/
public class ScaleFormatProcessor implements ExportAnnoProcessor {

    public Object process(Object value, ExportFieldBean exportFieldBean) throws ParameterException {
        if(null == value){
            return null;
        }
        Number data;
        try {
            data = (Number) value;
        }catch (ClassCastException e){
            throw new ParameterException(exportFieldBean.getFieldName()+":类型非数字格式！");
        }
        BigDecimal bigDecimal = new BigDecimal(data.floatValue());
        bigDecimal = bigDecimal.setScale(exportFieldBean.getScale(),BigDecimal.ROUND_HALF_UP);
        return bigDecimal;
    }
}
