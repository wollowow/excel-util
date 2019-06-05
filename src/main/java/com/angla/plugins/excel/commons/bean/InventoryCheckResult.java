package com.angla.plugins.excel.commons.bean;

/**
 * Title:InventoryCheckResult
 *
 * @author angla
 **/
public class InventoryCheckResult {

    private boolean checked;

    private String errMsg;

    public InventoryCheckResult(boolean checked, String errMsg) {
        this.checked = checked;
        this.errMsg = errMsg;
    }

    public static InventoryCheckResult suc() {
        return new InventoryCheckResult(true, null);
    }

    public static InventoryCheckResult fail(String errMsg) {
        return new InventoryCheckResult(false, errMsg);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
