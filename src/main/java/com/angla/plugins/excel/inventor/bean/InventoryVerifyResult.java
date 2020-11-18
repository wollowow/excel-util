package com.angla.plugins.excel.inventor.bean;

/**
 * Title:InventoryVerifyResult
 *
 * @author angla
 **/
public class InventoryVerifyResult {

    private boolean verified;

    private String errMsg;

    public InventoryVerifyResult(boolean checked, String errMsg) {
        this.verified = checked;
        this.errMsg = errMsg;
    }

    public static InventoryVerifyResult suc() {
        return new InventoryVerifyResult(true, null);
    }

    public static InventoryVerifyResult fail(String errMsg) {
        return new InventoryVerifyResult(false, errMsg);
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
