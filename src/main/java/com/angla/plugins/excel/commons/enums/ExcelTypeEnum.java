package com.angla.plugins.excel.commons.enums;

/**
 * excel文件类型
 */
public enum ExcelTypeEnum {

    EXCEL_2007(".xlsx", "504B0304",1048576),
    EXCEL_2003(".xls", "D0CF11E0",65536);

    ExcelTypeEnum(String suffix, String fileHeader, Integer maxSize) {
        this.suffix = suffix;
        this.fileHeader = fileHeader;
        this.maxSize=maxSize;
    }


    private String fileHeader;
    private String suffix;
    private Integer maxSize;
    public String getSuffix() {
        return suffix;
    }

    public String getFileHeader() {
        return fileHeader;
    }

    public Integer getMaxSize() {
        return maxSize;
    }
}
