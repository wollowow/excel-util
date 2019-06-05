package com.angla.plugins.excel.inventor.parse;

import com.angla.plugins.excel.commons.bean.InventorBeanTemplate;

/**
 * Title:Inventor
 *
 * @author angla
 **/
public interface Inventor <T extends InventorBeanTemplate>{

    void parse() throws Exception;

}
