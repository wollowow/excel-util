package com.angla.plugins.excel.export.processer;

import com.angla.plugins.excel.commons.throwable.exception.AnnotationException;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import com.angla.plugins.excel.export.anno.ExportFieldBean;

/**
 * @author angla
 * @create 2018-08-09 下午5:49
 * @desc 注解逻辑处理
 **/
public interface ExportAnnoProcessor {
    /**
     * 处理注解
     * @param value
     * @param exportFieldBean
     * @return
     * @throws ParameterException
     */
    Object process(Object value, ExportFieldBean exportFieldBean) throws ParameterException,
            IllegalAccessException, InstantiationException, AnnotationException;
}
