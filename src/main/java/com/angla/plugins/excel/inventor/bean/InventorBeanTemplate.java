package com.angla.plugins.excel.inventor.bean;

import com.angla.plugins.excel.export.anno.ExportField;

import java.io.Serializable;

/**
 * Title:InventorBeanTemplate
 *
 * @author angla
 **/
public class InventorBeanTemplate implements Serializable {


    private boolean correct = true;

    /*错误信息提示*/
    @ExportField(name = "错误信息")
    private StringBuilder errMsg = new StringBuilder();

    public String getErrMsg() {
        return errMsg.toString();
    }

    public void appendErrMsg(String errMsg) {
        if(errMsg.length() != 0){
            this.errMsg.append("|");
        }
        this.errMsg.append(errMsg);
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
