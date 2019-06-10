package com.angla.plugins.excel.inventor.parse.impl;

import com.angla.plugins.excel.commons.bean.InventorBeanTemplate;
import com.angla.plugins.excel.inventor.parse.AbstractInventor;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:ExcelInventor
 * 转换excel .xls格式文件
 * @author angla
 **/

public class ExcelInventor<T extends InventorBeanTemplate> extends AbstractInventor<T> {


    public ExcelInventor(Class<T> clazz) {
        super(clazz);
    }

    public ExcelInventor() {
    }

    public List<T> parse() throws Exception {
        return new ArrayList<>();
    }
}
