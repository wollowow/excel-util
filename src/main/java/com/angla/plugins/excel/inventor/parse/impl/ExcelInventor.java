package com.angla.plugins.excel.inventor.parse.impl;

import com.angla.plugins.excel.commons.bean.InventorBeanTemplate;
import com.angla.plugins.excel.commons.enums.CheckRuleEnum;
import com.angla.plugins.excel.inventor.format.CellValueFormater;
import com.angla.plugins.excel.inventor.format.DefaultCellValueFormater;
import com.angla.plugins.excel.inventor.parse.AbstractInventor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:ExcelInventor
 * 转换excel .xls格式文件
 * @author angla
 **/

public class ExcelInventor<T extends InventorBeanTemplate> extends AbstractInventor<T> {


    private final POIFSFileSystem fileSystem;

    public ExcelInventor(Class<T> clazz, POIFSFileSystem fileSystem, CheckRuleEnum checkRuleEnum) {
        super(clazz);
        super.formater = new DefaultCellValueFormater();
        super.checkRuleEnum = checkRuleEnum;
        this.fileSystem = fileSystem;

    }

    public ExcelInventor(Class<T> clazz, POIFSFileSystem fileSystem, CellValueFormater formater,CheckRuleEnum checkRuleEnum) {
        super(clazz);
        super.formater = formater;
        super.checkRuleEnum = checkRuleEnum;
        this.fileSystem = fileSystem;
    }

    public List<T> parse() throws Exception {
        Workbook workbook = new HSSFWorkbook(fileSystem,true);
        //todo
        return new ArrayList<>();
    }
}
