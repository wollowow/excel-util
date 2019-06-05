package com.angla.plugins.excel.inventor.anno;

import com.angla.plugins.excel.commons.bean.InventoryCheckResult;

/**
 * Title:CustomCheckRule
 *
 * @author liumenghua
 **/
public interface CustomCheckRule {

    InventoryCheckResult check(String value,InventorFieldBean inventorFieldBean);
}
