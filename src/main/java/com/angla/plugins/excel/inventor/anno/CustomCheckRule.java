package com.angla.plugins.excel.inventor.anno;

import com.angla.plugins.excel.commons.bean.InventoryVerifyResult;

/**
 * Title:CustomCheckRule
 *
 * @author liumenghua
 **/
public interface CustomCheckRule {

    InventoryVerifyResult check(String value);
}
