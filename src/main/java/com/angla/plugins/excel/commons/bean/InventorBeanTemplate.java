package com.angla.plugins.excel.commons.bean;

import java.io.Serializable;

/**
 * Title:InventorBeanTemplate
 *
 * @author angla
 **/
public class InventorBeanTemplate implements Serializable {

    /*错误信息提示*/
    private String errMsg;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
