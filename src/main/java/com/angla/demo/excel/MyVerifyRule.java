package com.angla.demo.excel;

import com.angla.plugins.excel.commons.bean.InventoryVerifyResult;
import com.angla.plugins.excel.inventor.anno.CustomCheckRule;

/**
 * Title:MyVerifyRule
 *
 * @author angla
 **/
public class MyVerifyRule implements CustomCheckRule {

    public InventoryVerifyResult check(String value) {

        if(value.startsWith("a")){
            return InventoryVerifyResult.suc();
        }
        return InventoryVerifyResult.fail("必须以a开头");
    }
}
