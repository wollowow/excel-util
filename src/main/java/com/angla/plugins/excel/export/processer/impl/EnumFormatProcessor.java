package com.angla.plugins.excel.export.processer.impl;

import com.angla.plugins.excel.commons.enums.ISEnum;
import com.angla.plugins.excel.commons.throwable.exception.AnnotationException;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import com.angla.plugins.excel.export.processer.ExportAnnoProcessor;
import com.angla.plugins.excel.export.anno.ExportFieldBean;

/**
 * 枚举类型处理
 * Title:EnumFormatProcessor
 *
 * @author angla
 **/
public class EnumFormatProcessor implements ExportAnnoProcessor {

    public Object process(Object value, ExportFieldBean exportFieldBean) throws
            ParameterException, AnnotationException {

        if (null == value) {
            return null;
        }
        Integer result;
        Class<? extends ISEnum> enumClass = exportFieldBean.getEnumRule();
        boolean flag = value instanceof Integer;
        if (!flag) {
            try {
                result = Integer.parseInt(value.toString());
            } catch (Exception e) {
                throw new ParameterException("注解key值必须为Integer类型");
            }
        } else {
            if (null == enumClass) {
                throw new AnnotationException("注解需要实现ISEnum接口");
            }
            if (!enumClass.isEnum()) {
                throw new ParameterException(enumClass.getName() + "非枚举类型");
            }
            result = (Integer) value;
        }

        ISEnum isEnum = resolveCustomEnum(result, enumClass);
        if (null == isEnum) {
            return "";
        }
        return isEnum.getName();
    }


    private ISEnum resolveCustomEnum(Integer value, Class<? extends ISEnum> type) {

        for (ISEnum constant : type.getEnumConstants()) {
            if (constant.getCode().equals(value)) {
                return constant;
            }
        }
        return null;
    }

}
