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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.server.ExportException;
import java.text.ParseException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * xlsx转成对象属性值
 */
public class ExcelXInventor<T extends InventorBeanTemplate> extends AbstractInventor<T> {

    /**
     * 解析sheet并转化object
     */
    private class SheetToObject implements MyXSSFSheetXMLHandler.SheetContentsHandler {

        private boolean firstCellOfRow;
        private boolean firstRow = true;
        private int currentRow = -1;
        private int currentCol = -1;


        private void outputMissingRows(int number) {
            currentRow += number;
        }


        @Override
        public void startRow(int rowNum) {
            // If there were gaps, continue
            outputMissingRows(rowNum - currentRow - 1);
            // Prepare for this row
            firstCellOfRow = true;
            currentRow = rowNum;
            currentCol = -1;
            try {
                t = clazz.newInstance();
            } catch (Exception e) {
                throw new ExcelException("解析失败!");
            }
        }

        @Override
        public void endRow(int rowNum) {
            if (!firstRow) {
                getResult().add(t);
            }
            if (firstRow) {
                firstRow = false;
            }
        }

        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment comment) {

            if (firstRow) {
                titles.add(formattedValue);
                return;
            }
            if (firstCellOfRow) {
                firstCellOfRow = false;
            }
            if (cellReference == null) {
                cellReference = new CellAddress(currentRow, currentCol).formatAsString();
            }

            currentCol = new CellAddress(cellReference).getColumn();

            try {
                if (CollectionUtils.isEmpty(titles)) {
                    throw new ExcelException("未查询到标题行");
                }
                String name = titles.get(currentCol);
                InventorFieldBean field = name2FieldMap.get(name);
                if (null == field) {
                    throw new ParseException("表头错误!",1);
                }
                InventoryVerifyResult checkResult = doProcess(formattedValue,field);
                if (!checkResult.isVerified()) {
                    t.setCorrect(false);
                    t.appendErrMsg(checkResult.getErrMsg());
                    if (checkRuleEnum.equals(CheckRuleEnum.BREAK_WHEN_ERROR)) {
                        throw new CellCheckException("校验失败:" + t.getErrMsg());
                    } else {
                        return;
                    }
                }
                String methodName =
                        "set" + field.getFieldName().substring(0, 1).toUpperCase() + field.getFieldName().substring(1);
                Method method = clazz.getMethod(methodName, field.getFieldType());
                method.setAccessible(true);
                method.invoke(t, formater.formatValue(formattedValue.trim(), field.getGeType().toString(),
                        field.getFormat()));
            } catch (IllegalAccessException | NoSuchMethodException
                    | InvocationTargetException | ParseException | InstantiationException e) {
                throw new ExcelException("解析异常!", e);
            }
        }
    }


    private final OPCPackage xlsxPackage;


    /**
     * OPCPackage
     *
     * @param pkg The XLSX package to process
     */
    public ExcelXInventor(OPCPackage pkg, Class<T> clazz, CheckRuleEnum checkRuleEnum) throws ExcelException {
        super(clazz);
        this.xlsxPackage = pkg;
        super.formater = new DefaultCellValueFormater();
        super.checkRuleEnum = checkRuleEnum;
    }

    /**
     * OPCPackage
     *
     * @param pkg The XLSX package to process
     */
    public ExcelXInventor(OPCPackage pkg, Class<T> clazz, CellValueFormater formater, CheckRuleEnum checkRuleEnum) throws ExcelException {
        super(clazz);
        this.xlsxPackage = pkg;
        super.formater = formater;
        super.checkRuleEnum = checkRuleEnum;
    }

    /**
     * 以sheet为维度解析
     *
     * @throws SAXException
     */
    public void parseSheet(
            Styles styles,
            SharedStrings strings,
            MyXSSFSheetXMLHandler.SheetContentsHandler sheetHandler,
            InputStream sheetInputStream) throws IOException, SAXException {

        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new MyXSSFSheetXMLHandler(
                    styles, name2FieldMap.size(), strings, sheetHandler, formatter);

            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch (ParserConfigurationException e) {
            throw new ExportException("解析器出错了！" + e.getMessage());
        }
    }

    /**
     * 解析并返回解析结果
     */
    public List<T> parse() throws IOException, OpenXML4JException, SAXException {
        MyReadOnlySharedStringsTable strings = new MyReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        while (iter.hasNext()) {
            try (InputStream stream = iter.next()) {
                parseSheet(styles, strings, new SheetToObject(), stream);
            }
        }
        return getResult();
    }


}