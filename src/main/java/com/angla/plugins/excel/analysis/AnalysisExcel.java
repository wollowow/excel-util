package com.angla.plugins.excel.analysis;

import cn.hutool.poi.excel.ExcelUtil;
import com.angla.plugins.excel.analysis.anno.LoadAnnoBean;
import com.angla.plugins.excel.analysis.anno.manager.AnnoManager;
import com.angla.plugins.excel.analysis.external.ResTypeEnum;
import com.angla.plugins.excel.analysis.external.Result;
import com.angla.plugins.excel.analysis.verify.BodyVerify;
import com.angla.plugins.excel.analysis.verify.ExcelVerify;
import com.angla.plugins.excel.analysis.verify.TitleVerify;
import com.angla.plugins.excel.commons.throwable.ExcelException;

import java.io.InputStream;
import java.util.List;

/**
 * @program: excel-util
 * @description: 读取excel
 * @author: angla
 * @create: 2018-08-02 17:42
 * @Version 1.0
 **/
public class AnalysisExcel {

    //加载bean
    LoadAnnoBean loadAnnoBean = new LoadAnnoBean();

    //解析excel
    public <T> Result<T> analysisAndVerify(InputStream is, Class<T> t) throws ExcelException {
        return analysisAndVerify(is, t, 0);
    }
    //解析excel
    public <T> Result<T> analysisAndVerify(InputStream is, Class<T> t, int startRow) throws ExcelException{
        return analysisAndVerify(is, t, startRow, Integer.MAX_VALUE);
    }
    //解析excel
    public <Bean> Result<Bean> analysisAndVerify(InputStream is, Class<Bean> t, int startRow, int endRow) throws ExcelException {
        if(is == null || t == null || startRow >= endRow || startRow < 0){
            return Result.err(ResTypeEnum.PARAMETER_ERR);
        }
        //解析excel
        List<List<Object>> dataList;
        // TODO: 2019/3/11
        try {
            dataList = ExcelUtil.getReader(is).read(startRow, endRow);
        }catch(Exception e){
            return Result.err(ResTypeEnum.EXCEL_ERR);
        }

        //开始校验
        return verify(dataList, t);
    }

    //校验
    public <Bean> Result<Bean> verify(List<List<Object>> dataList, Class<Bean> t) throws ExcelException {
        Result<Bean> res;

        //加载注解内容
        AnnoManager container = loadAnnoBean.load(t);

        res = ExcelVerify.verifyAndRecord(container, dataList);
        if(res != null){
            return res;
        }

        //校验标题
        boolean verifyResult = TitleVerify.verifyAndRecord(container, dataList.get(0));
        if(!verifyResult){
            res = Result.err(ResTypeEnum.TITLE_ERR);
            return res;
        }

        //校验内容
        res = BodyVerify.verifyAndRecord(container, dataList.subList(1, dataList.size()), t);

        //记录标题列
        res.setTitles(dataList.get(0));

        return res;
    }
}