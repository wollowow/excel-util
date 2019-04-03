package com.angla.demo.excel;

import com.angla.plugins.excel.ExcelFactory;
import com.angla.plugins.excel.analysis.external.ErrDetail;
import com.angla.plugins.excel.analysis.external.Result;
import com.angla.plugins.excel.commons.throwable.ExcelException;

import java.io.*;
import java.util.List;

/**
 * @program: excel-util
 * @description: excel演示类
 * @author: angla
 * @create: 2018-08-17 14:07
 * @Version 1.0
 **/
public class Main {

    public static void main(String[] args) throws IOException, ExcelException {
        Main main = new Main();

        //导入测试
        main.importTest();
    }

    public void importTest() throws IOException, ExcelException {
        long start = System.currentTimeMillis();
        String path = "C:\\Users\\Administrator\\Desktop\\上传插件10.xlsx";
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);

        //获取注解
        Result<ImportBean> res = ExcelFactory.getAnalysisExcel().analysisAndVerify(fis, ImportBean.class);
        fis.close();

        List<ImportBean>    successList = res.getSuccessList();
        List<List<Object>>  errorList   = res.getErrorList();
        List<Object>        titles      = res.getTitles();
        List<ErrDetail>     errDetail   = res.getErrDetail();
        List<List<Object>>  errorListAndMsg = res.getErrorListAndMsg();

    }
}
