package com.angla.plugins.excel.analysis.verify;

import com.angla.plugins.excel.analysis.anno.manager.AnnoManager;
import com.angla.plugins.excel.analysis.external.ResTypeEnum;
import com.angla.plugins.excel.analysis.external.Result;

import java.util.List;

/**
 * @program: excel-util
 * @description: 表校验
 * @author: angla
 * @create: 2018-08-21 15:32
 * @Version 1.0
 **/
public class ExcelVerify {

    public static Result verifyAndRecord(AnnoManager container, List<List<Object>> dataList) {
        if(dataList == null){
            return Result.err(ResTypeEnum.EXCEL_EMPTY_ERR);
        }
        if(dataList.size() < container.getMinRow()){
            return Result.err(ResTypeEnum.EXCEL_MIN_ROW_ERR);
        }
        if(dataList.size() > container.getMaxRow()){
            return Result.err(ResTypeEnum.EXCEL_MAX_ROW_ERR);
        }
        if(dataList.get(0).size() < container.getMinColumn()){
           return Result.err(ResTypeEnum.EXCEL_MIN_COLUMN_ERR);
        }
        if(dataList.get(0).size() > container.getMaxColumn()){
            return Result.err(ResTypeEnum.EXCEL_MAX_COLUMN_ERR);
        }
        return null;
    }
}
