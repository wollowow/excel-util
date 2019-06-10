package com.angla.plugins.excel.inventor.parse;

import com.angla.plugins.excel.commons.bean.InventorBeanTemplate;

import java.util.List;

/**
 * Title:Inventor
 *
 * @author angla
 **/
public interface Inventor <T extends InventorBeanTemplate>{

    List<T> parse() throws Exception;

}
