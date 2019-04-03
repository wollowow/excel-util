package com.angla.demo.excel;

import com.angla.plugins.excel.analysis.anno.interfaces.RowVerify;
import com.angla.plugins.excel.analysis.external.ErrDetail;

import java.util.List;

/**
 * @program: excel-util
 * @description: 行验证
 * @author: angla
 * @create: 2018-08-08 09:45
 * @Version 1.0
 **/
public class VerifyRow implements RowVerify<ImportBean> {
    @Override
    public ErrDetail beforeVerify(List<Object> row){
        if(row.get(1).equals("你好")){
            return new ErrDetail("名称不能是你好");
        }
        return null;
    }

    @Override
    public ErrDetail afterVerify(ImportBean bean){
        if(bean.getId() == 0L){
            return new ErrDetail("id异常");
        }
        return null;
    }
}
