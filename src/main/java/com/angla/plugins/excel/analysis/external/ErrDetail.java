package com.angla.plugins.excel.analysis.external;

/**
 * @program: excel-util
 * @description: 错误原因对象
 * @author: angla
 * @create: 2018-08-02 17:59
 * @Version 1.0
 **/
public class ErrDetail {
    private ErrDetailTypeEnum errType;          //错误类型
    private String msg;                         //错误原因
    private Integer row;                            //错误所在行
    private Integer col;                            //错误所在列
    private String name;                        //错误所在列的别名
    private String dbName;                      //错误所在列的字段名

    public ErrDetail(ErrDetailTypeEnum errType, String msg, Integer row, Integer col, String name, String dbName) {
        this.errType = errType;
        this.msg = msg;
        this.row = row;
        this.col = col;
        this.name = name;
        this.dbName = dbName;
    }

    public ErrDetail(String msg) {
        this.msg = msg;
    }

    public ErrDetailTypeEnum getErrType() {
        return errType;
    }

    public void setErrType(ErrDetailTypeEnum errType) {
        this.errType = errType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}