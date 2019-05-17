package com.angla.plugins.excel;

import com.angla.plugins.excel.commons.throwable.ExcelException;
import com.angla.plugins.excel.commons.throwable.exception.AnnotationException;
import com.angla.plugins.excel.inventor.Student;
import com.angla.plugins.excel.inventor.anno.InventorField;
import com.angla.plugins.excel.inventor.format.DefaultCellValueFormater;
import com.angla.plugins.excel.inventor.format.CellValueFormater;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.server.ExportException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

/**
 * xlsx转成对象属性值
 */
public class XLSX2Object<T> {
    /**
     * Uses the XSSF Event SAX helpers to do most of the work
     * of parsing the Sheet XML, and outputs the contents
     * as a (basic) CSV.
     */
    private class SheetToCSV implements SheetContentsHandler {

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
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ExcelException("生成带转换的类异常!");
            }
        }

        @Override
        public void endRow(int rowNum) {
            if (!firstRow) {
                result.add(t);
                t = null;
            }
            if (firstRow) {
                firstRow = false;
            }
        }

        @Override
        public void cell(String cellReference, String formattedValue,
                         XSSFComment comment) {

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

            currentCol = (int) (new CellReference(cellReference)).getCol();

            try {
                if (CollectionUtils.isEmpty(titles)) {
                    throw new ExcelException("未查询到标题行");
                }
                String name = titles.get(currentCol);
                Field field = name2FieldMap.get(name);
                if (null == field) {
                    return;
                }
                String methodName =
                        "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                Method method = clazz.getMethod(methodName, field.getType());
                method.setAccessible(true);
                method.invoke(t, formater.formatValue(formattedValue, field.getGenericType().toString()));
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | ParseException e) {
                throw new ExcelException("解析异常!", e);
            }
        }
        public void headerFooter(String s, boolean b, String s1) {
        }
    }


    private final OPCPackage xlsxPackage;


    /**
     * Destination for data
     */
    private final List<T> result = new ArrayList<>();

    public List<T> getResult() {
        return result;
    }

    private T t;

    /**
     * 标题
     */
    private List<String> titles = new ArrayList<>();

    public List<String> getTitles() {
        return titles;
    }

    private CellValueFormater formater;

    private Class<T> clazz;

    /**
     * 名称和属性关联
     */
    private Map<String, Field> name2FieldMap = new HashMap<>();


    private void buildNameAndField() throws AnnotationException {

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            InventorField inventorField = field.getAnnotation(InventorField.class);
            if (null == inventorField) {
                continue;
            }
            String name = inventorField.name();
            if ("".equals(name)) {
                throw new AnnotationException("注解name不能为空");
            }
            name2FieldMap.put(name, field);
        }

        if (CollectionUtils.isEmpty(name2FieldMap.entrySet())) {
            throw new AnnotationException("需要为转化的属性配置@InventorField注解");
        }
    }

    /**
     * Creates a new XLSX -> CSV examples
     *
     * @param pkg The XLSX package to process
     */
    public XLSX2Object(OPCPackage pkg, CellValueFormater formater, Class<T> clazz) throws AnnotationException {
        this.xlsxPackage = pkg;
        this.clazz = clazz;
        this.formater = formater;
        buildNameAndField();
    }

    /**
     * Creates a new XLSX -> CSV examples
     *
     * @param pkg The XLSX package to process
     */
    public XLSX2Object(OPCPackage pkg, Class<T> clazz) throws AnnotationException {
        this.xlsxPackage = pkg;
        this.clazz = clazz;
        this.formater = new DefaultCellValueFormater();
        buildNameAndField();
    }

    /**
     * 以sheet为维度解析
     *
     * @throws SAXException
     */
    public void processSheet(
            Styles styles,
            SharedStrings strings,
            SheetContentsHandler sheetHandler,
            InputStream sheetInputStream) throws IOException, SAXException {

        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(
                    styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch (ParserConfigurationException e) {
            throw new ExportException("解析器出错了！" + e.getMessage());
        }
    }

    /**
     * Initiates the processing of the XLS workbook file to CSV.
     *
     * @throws IOException  If reading the data from the package fails.
     * @throws SAXException if parsing the XML data fails.
     */
    public void process() throws IOException, OpenXML4JException, SAXException {
        MyReadOnlySharedStringsTable strings = new MyReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        while (iter.hasNext()) {
            try (InputStream stream = iter.next()) {
                processSheet(styles, strings, new SheetToCSV(), stream);
            }
        }
    }


    public static void main(String[] args) throws Exception {

        Long start = System.currentTimeMillis();
        File xlsxFile = new File("/Users/menghualiu/Desktop/test.xlsx");
        if (!xlsxFile.exists()) {
            System.err.println("Not found or not a file: " + xlsxFile.getPath());
            return;
        }

        // The package open is instantaneous, as it should be.
        try (OPCPackage p = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ)) {
            XLSX2Object<Student> xlsx2csv = new XLSX2Object<>(p, Student.class);
            xlsx2csv.process();

            for (Student s : xlsx2csv.getResult()) {
                System.out.println(s.toString());
            }
        }
        Long end = System.currentTimeMillis();
        System.out.println("共耗时:" + (end - start));

    }
}