package com.angla.plugins.excel.inventor.processer;

import com.angla.plugins.excel.commons.bean.InventoryVerifyResult;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import com.angla.plugins.excel.inventor.anno.InventorFieldBean;

/**
 * @author angla
 * @create 2018-08-09 下午5:49
 * @desc 注解逻辑处理
 **/
public interface InventorAnnoProcessor {
    /**
     * 处理注解
     * @param value
     * @param inventorFieldBean
     * @return
     * @throws ParameterException
     */
    InventoryVerifyResult checked(String value, InventorFieldBean inventorFieldBean);

}
