package com.angla.plugins.excel.analysis.external;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: excel-util
 * @description：解析返回原因
 * @author: angla
 * @create: 2018-08-02 17:52
 * @Version 1.0
 **/
public class Result<T> {
    private ResTypeEnum resType;                    //返回类型 1 已处理完成 2 参数错误 3 excel为空 4excel表头验证未通过

    private List<Object> titles;                    //标题行内容

    private List<T> successList;                    //正确数据

    private List<List<Object>> errorList;           //错误数据

    private List<ErrDetail> errDetail;              //错误详情

    private List<List<Object>> errorListAndMsg;     //错误数据和原因

    public static Result err(ResTypeEnum resType){
        if(resType == ResTypeEnum.FINISH){
            return null;
        }
        return new Result(resType);
    }
    public static Result suc(int size){
        Result res = new Result(ResTypeEnum.FINISH);
        res.successList = new ArrayList(size);
        res.errorList = new ArrayList(size);
        res.errDetail = new ArrayList(size);
        res.errorListAndMsg = new ArrayList(size);
        return res;
    }

    public Result(){}

    public Result(ResTypeEnum resType){
        this.resType = resType;
    }

    public void addErrDetail(ErrDetail errDetail) {
        this.errDetail.add(errDetail);
    }
    public void addErrorList(List<Object> errorList) {
        this.errorList.add(errorList);
    }
    public void addErrorListAndMsg(List<Object> errorList, ErrDetail detail){
        this.errorListAndMsg.add(new ArrayList<Object>(){{
                addAll(errorList);
                add(detail.getMsg());
        }});
    }
    public void addSuccessList(T successBean) {
        this.successList.add(successBean);
    }

    /*getter and setter*/
    public void setTitles(List<Object> titles) {
        this.titles = titles;
    }

    public List<T> getSuccessList() {
        return successList;
    }
    public List<Object> getTitles() {
        return titles;
    }
    public List<List<Object>> getErrorList() {
        return errorList;
    }
    public List<ErrDetail> getErrDetail() {
        return errDetail;
    }

    public ResTypeEnum getResType() {
        return resType;
    }

    public void setResType(ResTypeEnum resType) {
        this.resType = resType;
    }

    public List<List<Object>> getErrorListAndMsg() {
        return errorListAndMsg;
    }

    public void setErrorListAndMsg(List<List<Object>> errorListAndMsg) {
        this.errorListAndMsg = errorListAndMsg;
    }
}