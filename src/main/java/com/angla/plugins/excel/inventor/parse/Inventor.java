package com.angla.plugins.excel.inventor.parse;

import com.angla.plugins.excel.commons.bean.InventorBeanTemplate;
import com.angla.plugins.excel.commons.bean.InventorParseResult;

/**
 * Title:Inventor
 *
 * @author angla
 **/
public interface Inventor <T extends InventorBeanTemplate>{

    InventorParseResult parse() throws Exception;

}
