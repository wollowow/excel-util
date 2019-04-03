package com.angla.plugins.excel.analysis.anno.manager;

import com.angla.plugins.excel.analysis.anno.AnalysisField;
import com.angla.plugins.excel.analysis.anno.interfaces.FieldTranform;

/**
 * @program: excel-util
 * @description: 字段注解信息
 * @author: angla
 * @create: 2018-08-02 14:47
 * @Version 1.0
 **/
public class AnnoField {
    private String fieldName;       //字段名
    private Class fieldType;        //该列的类型

    private int    colNum;          //对应excel中第几列

    private String colName;         //字段注释名
    private boolean must;           //是否必填
    private String regex;           //自定义验证
    private int[] uniNos;           //是否唯一
    private Class tranform;         //定义转换格式
    private Object tranformObj;     //转换格式的对象

    protected AnnoField(String fieldName, Class fieldType){
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    protected void setExplain(AnalysisField explain) throws IllegalAccessException, InstantiationException {
        if(explain != null){
            this.colName = explain.name();
            this.must = explain.must();
            this.regex = explain.regex();
            this.uniNos = explain.uniNos();

            this.tranform = explain.transform();
            //如果tranform实现了FieldTranform并且是class
            if(tranform != null && tranform != FieldTranform.class && FieldTranform.class.isAssignableFrom(explain.transform())) {
                this.tranformObj = tranform.newInstance();
            }
        }
    }



    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

    public boolean isMust() {
        return must;
    }

    public int[] getUniNos() {
        return uniNos;
    }

    public String getRegex() {
        return regex;
    }

    public Class getTranform() {
        return tranform;
    }

    public Class getFieldType() {
        return fieldType;
    }

    public Object getTranformObj() {
        return tranformObj;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }
}
