package com.angla.plugins.excel.inventor.parse.impl;

import com.angla.plugins.excel.commons.bean.InventorBeanTemplate;
import com.angla.plugins.excel.commons.bean.InventoryVerifyResult;
import com.angla.plugins.excel.commons.enums.CheckRuleEnum;
import com.angla.plugins.excel.commons.throwable.ExcelException;
import com.angla.plugins.excel.commons.throwable.exception.CellCheckException;
import com.angla.plugins.excel.inventor.anno.InventorFieldBean;
import com.angla.plugins.excel.inventor.format.CellValueFormater;
import com.angla.plugins.excel.inventor.format.DefaultCellValueFormater;
import com.angla.plugins.excel.inventor.parse.AbstractInventor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.List;

/**
 * Title:ExcelInventor
 * 转换excel .xls格式文件
 *
 * @author angla
 **/

public class ExcelInventor<T extends InventorBeanTemplate> extends AbstractInventor<T> {


    private final POIFSFileSystem fileSystem;

    private boolean isFirstRow = true;


    private final DataFormatter formatter ;

    public ExcelInventor(Class<T> clazz, POIFSFileSystem fileSystem, CheckRuleEnum checkRuleEnum) {
        super(clazz);
        super.formater = new DefaultCellValueFormater();
        super.checkRuleEnum = checkRuleEnum;
        this.fileSystem = fileSystem;
        this.formatter = new DataFormatter();

    }

    public ExcelInventor(Class<T> clazz, POIFSFileSystem fileSystem, CellValueFormater formater,
                         CheckRuleEnum checkRuleEnum) {
        super(clazz);
        super.formater = formater;
        super.checkRuleEnum = checkRuleEnum;
        this.fileSystem = fileSystem;
        this.formatter = new DataFormatter();
    }

    public List<T> parse() throws Exception {
        Workbook workbook = new HSSFWorkbook(fileSystem, true);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            parseSheet(sheet);
        }
        return getResult();
    }


    /**
     * 解析 sheet
     *
     * @param sheet
     */
    private void parseSheet(Sheet sheet) throws InstantiationException,
            IllegalAccessException,
            NoSuchMethodException,
            ParseException,
            InvocationTargetException {
        int rowNum = sheet.getLastRowNum();
        if (rowNum < 0) {
            return;
        }
        for (int i = 0; i < rowNum; i++) {
            Row row = sheet.getRow(i);
            if (null == row) {
                continue;
            }
            t = clazz.newInstance();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (isFirstRow) {
                    if (null == cell || cell.getCellType().equals(CellType.BLANK)) {
                        throw new ExcelException("表头错误");
                    }
                    titles.add(String.valueOf(cell.getStringCellValue()));
                    continue;
                }
                String name = titles.get(j);
                InventorFieldBean field = name2FieldMap.get(name);

                String formattedValue = getCellValue(cell);
                InventoryVerifyResult checkResult = doProcess(formattedValue, field);
                if (!checkResult.isVerified()) {
                    t.setCorrect(false);
                    t.appendErrMsg(checkResult.getErrMsg());
                    if (checkRuleEnum.equals(CheckRuleEnum.BREAK_WHEN_ERROR)) {
                        throw new CellCheckException("校验失败:" + t.getErrMsg());
                    } else {
                        continue;
                    }
                }
                String methodName =
                        "set" + field.getFieldName().substring(0, 1).toUpperCase() + field.getFieldName().substring(1);
                Method method = clazz.getMethod(methodName, field.getFieldType());
                method.setAccessible(true);
                method.invoke(t, formater.formatValue(formattedValue.trim(), field.getGeType().toString(),
                        field.getFormat()));
                getResult().add(t);
            }
            if (isFirstRow) {
                isFirstRow = false;
            }
        }
    }


    /**
     * 获取单元格值
     *
     * @param cell
     * @return
     */
    private String getCellValue(Cell cell) {

        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        short formatIndex = cell.getCellStyle().getDataFormat();
        String formatString =  cell.getCellStyle().getDataFormatString();
        switch (cell.getCellType()) {
            case NUMERIC:
                String n = String.valueOf(cell.getNumericCellValue());
                if (formatString != null && n.length() > 0)
                    cellValue = formatter.formatRawCellContents(Double.parseDouble(n), formatIndex,
                            formatString);
                else
                    cellValue = n;
                break;
            case STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK:
                cellValue = "";
                break;
            case ERROR:
                cellValue = String.valueOf(cell.getErrorCellValue());
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;

    }
}
