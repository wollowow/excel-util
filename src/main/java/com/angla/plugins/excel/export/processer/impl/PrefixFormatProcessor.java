package com.angla.plugins.excel.export.processer.impl;

import com.angla.plugins.excel.export.processer.ExportAnnoProcessor;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import com.angla.plugins.excel.export.anno.ExportFieldBean;

/**
 * 时间格式导出
 * Title:DateFormatProcessor
 *
 * @author angla
 **/
public class PrefixFormatProcessor implements ExportAnnoProcessor {
    public String process(Object value, ExportFieldBean exportField) throws ParameterException {
        value = value == null ? "":value;
       return exportField.getPrefix() + value;
    }
}
