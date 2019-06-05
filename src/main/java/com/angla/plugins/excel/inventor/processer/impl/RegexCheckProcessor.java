package com.angla.plugins.excel.inventor.processer.impl;

import com.angla.plugins.excel.commons.bean.InventoryCheckResult;
import com.angla.plugins.excel.inventor.anno.InventorFieldBean;
import com.angla.plugins.excel.inventor.processer.InventorAnnoProcessor;

/**
 * Title:RequiredCheckProcessor
 *
 * @author angla
 **/

public class RegexCheckProcessor implements InventorAnnoProcessor {

    @Override
    public InventoryCheckResult checked(String value, InventorFieldBean inventorFieldBean)  {
        if(value.matches(inventorFieldBean.getRegex())){
            return InventoryCheckResult.suc();
        }
        return InventoryCheckResult.fail("正则匹配失败");
    }
}
