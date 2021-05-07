package com.angla.plugins.excel;

import com.angla.plugins.excel.inventor.bean.InventorBeanTemplate;
import com.angla.plugins.excel.commons.enums.CheckRuleEnum;
import com.angla.plugins.excel.commons.enums.ExcelTypeEnum;
import com.angla.plugins.excel.commons.throwable.ExcelException;
import com.angla.plugins.excel.commons.throwable.exception.ExcelEmptyException;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import com.angla.plugins.excel.export.ExcelExporter;
import com.angla.plugins.excel.export.ExcelXExporter;
import com.angla.plugins.excel.export.Exporter;
import com.angla.plugins.excel.inventor.format.CellValueFormater;
import com.angla.plugins.excel.inventor.format.DefaultCellValueFormater;
import com.angla.plugins.excel.inventor.parse.Inventor;
import com.angla.plugins.excel.inventor.parse.impl.ExcelInventor;
import com.angla.plugins.excel.inventor.parse.impl.ExcelXInventor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @program: excel-util
 * @description: 工厂类
 * @author: angla
 * @create: 2018-08-02 17:40
 * @Version 1.0
 **/
public class ExcelFactory {


    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return Exporter
     */
    private static <T> Exporter<T> initExporter(List<T> data, ExcelTypeEnum excelEnum,
                                                List<String> columns, boolean showErrMsg) throws ParameterException {
        if (CollectionUtils.isEmpty(data)) {
            throw new ParameterException("导出数据不能为空");
        }
        if (ExcelTypeEnum.EXCEL_XLS.equals(excelEnum) && data.size() <= ExcelTypeEnum.EXCEL_XLS.getMaxSize()) {
            //数据量超过xls文件格式最大值时用xlsx文件格式进行导出
            return new ExcelExporter<>(data, columns, showErrMsg);
        }
        return new ExcelXExporter<>(data, columns, showErrMsg);
    }

    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return Exporter
     */
    public static <T> Exporter<T> initExporter(List<T> data) throws ParameterException {
        return initExporter(data, false);
    }


    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return Exporter
     */
    public static <T> Exporter<T> initExporter(List<T> data, boolean showErrMsg) throws ParameterException {
        return initExporter(data, null, null, showErrMsg);
    }


    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return Exporter
     */
    public static <T> Exporter<T> initExporter(List<T> data, List<String> columns)
            throws ParameterException {
        return initExporter(data, null, columns, false);
    }

    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return Exporter
     */
    public static <T> Exporter<T> initExporter(List<T> data, List<String> columns, boolean showErrMsg)
            throws ParameterException {
        return initExporter(data, null, columns, showErrMsg);
    }


    /**
     * 初始化导出工具
     *
     * @param data 导出数据
     * @return Exporter
     */
    public static <T> Exporter<T> initExporter(List<T> data, ExcelTypeEnum excelEnum, boolean showErrMsg)
            throws ParameterException {
        return initExporter(data, excelEnum, null, showErrMsg);
    }


    /**
     * 初始化导入工具，默认失败后直接返回错误
     *
     * @param <T>
     * @return Inventor
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(File file, Class<T> tClass)
            throws IOException {
        return initInventor(file, tClass, CheckRuleEnum.CONTINUE_WHEN_ERROR);
    }


    /**
     * 初始化导入工具
     *
     * @param <T>
     * @return Inventor
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(File file, Class<T> tClass,
                                                                            CheckRuleEnum checkRuleEnum)
            throws IOException {

        return initInventor(file, tClass, new DefaultCellValueFormater(), checkRuleEnum);
    }


    /**
     * 初始化导入工具，默认失败后直接返回错误
     *
     * @param <T>
     * @return Inventor
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(File file, CellValueFormater formater,
                                                                            Class<T> tClass) throws IOException {
        return initInventor(file, tClass, formater, CheckRuleEnum.CONTINUE_WHEN_ERROR);
    }


    /**
     * 初始化导入工具
     *
     * @param <T>
     * @return Inventor
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(File file, Class<T> tClass,
                                                                            CellValueFormater formater,
                                                                            CheckRuleEnum checkRuleEnum
    ) throws IOException {
        return initInventor(file, tClass, formater, checkRuleEnum, -1);

    }

    /**
     * 初始化导入工具
     *
     * @param <T>
     * @return Inventor
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(File file, Class<T> tClass,
                                                                            CellValueFormater formater,
                                                                            CheckRuleEnum checkRuleEnum,
                                                                            int sheetIndex) throws IOException {
        String fileHeader = getFileHeader(file);
        if (null == fileHeader || "".equals(fileHeader)) {
            throw new ExcelException("未知文件类型");
        }
        InputStream inputStream = new FileInputStream(file);
        return initInventor(inputStream, tClass, formater, checkRuleEnum, sheetIndex);
    }


    /**
     * 初始化导入工具
     *
     * @param inputStream
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(InputStream inputStream, Class<T> tClass,
                                                                            int sheetIndex)
            throws IOException {
        return initInventor(inputStream, tClass, new DefaultCellValueFormater(), CheckRuleEnum.CONTINUE_WHEN_ERROR,
                sheetIndex);
    }

    /**
     * 初始化导入工具
     *
     * @param inputStream
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(InputStream inputStream, Class<T> tClass)
            throws IOException {
        return initInventor(inputStream, tClass, CheckRuleEnum.CONTINUE_WHEN_ERROR);
    }

    /**
     * 初始化导入工具
     *
     * @param inputStream   输入流
     * @param tClass        转化类
     * @param checkRuleEnum 校验类型
     * @param <T>
     * @return
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(InputStream inputStream, Class<T> tClass,
                                                                            CheckRuleEnum checkRuleEnum) throws IOException {
        return initInventor(inputStream, tClass, new DefaultCellValueFormater(), checkRuleEnum, -1);
    }

    /**
     * 初始化导入工具
     *
     * @param inputStream
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(InputStream inputStream,
                                                                            CellValueFormater formater,
                                                                            Class<T> tClass) throws IOException {
        return initInventor(inputStream, tClass, formater, CheckRuleEnum.CONTINUE_WHEN_ERROR, -1);
    }

    /**
     * 初始化导入工具
     *
     * @param inputStream   输入流
     * @param tClass        转化类
     * @param checkRuleEnum 校验类型
     * @param <T>
     * @return
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(InputStream inputStream, Class<T> tClass,
                                                                            CellValueFormater formater,
                                                                            CheckRuleEnum checkRuleEnum,
                                                                            int sheetIndex) throws IOException {
        if (null == inputStream) {
            throw new ExcelEmptyException("空文件流");
        }
        Inventor<T> inventor;
        OPCPackage pkg;
        POIFSFileSystem fileSystem;
        byte[] data = IOUtils.toByteArray(inputStream);
        try{
            InputStream xlsxStream = new ByteArrayInputStream(data);
            pkg = OPCPackage.open(xlsxStream);
            xlsxStream.close();
        } catch (NotOfficeXmlFileException e) {
            InputStream xlsxStream = new ByteArrayInputStream(data);
            fileSystem = new POIFSFileSystem(xlsxStream);
            xlsxStream.close();
            return new ExcelInventor<>(tClass, fileSystem, formater, checkRuleEnum, sheetIndex);
        } catch (Exception e) {
            throw new ExcelException("文件解析错误");
        }
        inventor = new ExcelXInventor<>(pkg, tClass, formater, checkRuleEnum, sheetIndex);
        return inventor;
    }


    /**
     * 初始化导入工具
     *
     * @param filePath 文件路径
     * @param tClass   转化类
     * @param <T>
     * @return
     * @throws InvalidFormatException
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(String filePath,
                                                                            Class<T> tClass) throws InvalidFormatException, IOException {
        return initInventor(filePath, tClass, CheckRuleEnum.CONTINUE_WHEN_ERROR);
    }


    /**
     * 初始化导入工具
     *
     * @param filePath      文件路径
     * @param tClass        转化类
     * @param checkRuleEnum 校验类型
     * @param <T>
     * @return
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(String filePath, Class<T> tClass,
                                                                            CheckRuleEnum checkRuleEnum)
            throws IOException {

        return initInventor(filePath, tClass, new DefaultCellValueFormater(), checkRuleEnum, -1);
    }

    /**
     * 初始化导入工具
     *
     * @param filePath 文件路径
     * @param tClass   转化类
     * @param <T>
     * @return
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(String filePath,
                                                                            Class<T> tClass,
                                                                            CellValueFormater formater) throws IOException {

        return initInventor(filePath, tClass, formater, CheckRuleEnum.CONTINUE_WHEN_ERROR, -1);
    }


    /**
     * 初始化导入工具
     *
     * @param filePath      文件路径
     * @param tClass        转化类
     * @param checkRuleEnum 校验类型
     * @param <T>
     * @return
     */
    public static <T extends InventorBeanTemplate> Inventor<T> initInventor(String filePath, Class<T> tClass,
                                                                            CellValueFormater formater,
                                                                            CheckRuleEnum checkRuleEnum,
                                                                            int sheetIndex) throws IOException {

        if (null == filePath || "".equals(filePath)) {
            throw new ExcelEmptyException("空的文件路径");
        }
        File file = new File(filePath);
        return initInventor(file, tClass, formater, checkRuleEnum, sheetIndex);
    }

    /**
     * 读取文件头信息获取文件类型
     *
     * @param file
     * @return
     */
    private static String getFileHeader(File file) {
        if (!file.exists()) {
            throw new ExcelException("文件不存在");
        }
        if (file.isDirectory()) {
            throw new ExcelException("不能为目录");
        }
        String value = null;
        try (FileInputStream is = new FileInputStream(file)) {
            value = getByteHeader(is);
        } catch (Exception e) {
        }
        return value;
    }


    private static String getByteHeader(InputStream inputStream) throws IOException {
        String value;
        byte[] b = new byte[4];
        inputStream.read(b, 0, b.length);
        value = bytesToHexString(b);
        return value;
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (byte b : src) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(b & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

}
