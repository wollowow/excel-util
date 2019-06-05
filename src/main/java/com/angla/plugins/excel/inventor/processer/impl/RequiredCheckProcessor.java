package com.angla.plugins.excel.inventor.processer.impl;

import com.angla.plugins.excel.commons.bean.InventoryCheckResult;
import com.angla.plugins.excel.inventor.anno.InventorFieldBean;
import com.angla.plugins.excel.inventor.processer.InventorAnnoProcessor;

/**
 * Title:RequiredCheckProcessor
 *
 * @author angla
 **/

public class RequiredCheckProcessor implements InventorAnnoProcessor {

    @Override
    public InventoryCheckResult checked(String value, InventorFieldBean inventorFieldBean) {

        if (inventorFieldBean.isRequired() && (null == value || "".equals(value))) {
            return InventoryCheckResult.fail(inventorFieldBean.getName() + "为必填字段!");
        }
        return InventoryCheckResult.suc();
    }
}
