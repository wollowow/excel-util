package com.angla.demo.excel;

import com.angla.plugins.excel.analysis.anno.interfaces.FieldTranform;

/**
 * @program: excel-util
 * @description: 转换type类型
 * @author: angla
 * @create: 2018-08-09 14:03
 * @Version 1.0
 **/
public class TranType implements FieldTranform {

    @Override
    public Integer tranform(Object obj) {
        String type = (obj+"").trim();
        if("是".equals(type)){
            return 1;
        }else if("否".equals(type)){
            return 2;
        }
        return 0;
    }
}
