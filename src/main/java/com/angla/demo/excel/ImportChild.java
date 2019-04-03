package com.angla.demo.excel;

import com.angla.plugins.excel.analysis.anno.AnalysisField;
import com.angla.plugins.excel.commons.constant.RegexConstant;

/**
 * @program: excel-util
 * @description: 导入bean子类
 * @author: angla
 * @create: 2018-08-20 13:24
 * @Version 1.0
 **/
public class ImportChild {

    @AnalysisField(name="123", must=true, regex= RegexConstant.NON_NEGATIVE_INTEGER)
    private Integer other;

    @AnalysisField(name="数字")
    private Boolean isNum;

    public Integer getOther() {
        return other;
    }
    public void setOther(Integer other) {
        this.other = other;
    }
    public Boolean getNum() {
        return isNum;
    }
    public void setNum(Boolean num) {
        isNum = num;
    }
}
