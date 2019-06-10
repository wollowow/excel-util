package com.angla.plugins.excel.commons.bean;

import org.apache.poi.ss.util.CellAddress;

/**
 * Title:XCell
 * 记录单元格地址和值
 * @author angla
 **/
public class XCell {

    private String cellRef;

    private String value;

    public XCell(String cellRef, String value) {
        this.cellRef = cellRef;
        this.value = value;
    }

    public XCell(CellAddress cellAddress, String value) {
        this.cellRef = cellAddress.formatAsString();
        this.value = value;
    }

    public XCell(int row, int col, String value) {
        this.cellRef = new CellAddress(row, col).formatAsString();
        this.value = value;
    }

    public String getCellRef() {
        return cellRef;
    }

    public void setCellRef(String cellRef) {
        this.cellRef = cellRef;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
