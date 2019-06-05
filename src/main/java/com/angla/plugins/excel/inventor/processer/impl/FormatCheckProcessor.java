package com.angla.plugins.excel.inventor.processer.impl;

import com.angla.plugins.excel.commons.bean.InventoryCheckResult;
import com.angla.plugins.excel.inventor.anno.InventorFieldBean;
import com.angla.plugins.excel.inventor.processer.InventorAnnoProcessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Title:RequiredCheckProcessor
 *
 * @author angla
 **/

public class FormatCheckProcessor implements InventorAnnoProcessor {

    @Override
    public InventoryCheckResult checked(String value, InventorFieldBean inventorFieldBean)  {
        SimpleDateFormat sf = new SimpleDateFormat(inventorFieldBean.getFormat());
        try {
            sf.parse(value);
        } catch (ParseException e) {
            return InventoryCheckResult.fail(inventorFieldBean.getName()+"时间格式错误");
        }
        return InventoryCheckResult.suc();
    }
}
