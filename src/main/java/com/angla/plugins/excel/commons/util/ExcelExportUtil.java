package com.angla.plugins.excel.commons.util;

import com.angla.plugins.excel.export.anno.ExportField;
import com.angla.plugins.excel.export.anno.SuperInclude;
import com.angla.plugins.excel.inventor.anno.InventorField;
import com.angla.plugins.excel.inventor.bean.InventorBeanTemplate;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.util.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 导出工具类
 * Title:ExcelExportUtil
 *
 * @author liumenghua
 **/
public class ExcelExportUtil {


    /**
     * 通过格式校验但未通过业务校验的数据可以转换成errorList
     *
     * @param dataList
     * @return
     * @throws IllegalAccessException
     */
    public static List<List<String>> beans2Errors(List<? extends InventorBeanTemplate> dataList, List<String> titles) throws IllegalAccessException {
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        Class tClass = dataList.get(0).getClass();
        Annotation superInclude = tClass.getAnnotation(SuperInclude.class);
        Field[] fields;
        List<List<String>> lines = new LinkedList<>();
        for (Object t : dataList) {
            List<String> line = new ArrayList<>();
            if (null == superInclude) {
                fields = tClass.getDeclaredFields();
            } else {
                List<List<Field>> fieldGroup = new ArrayList<>();
                for (Class<?> clazz = tClass; clazz != Object.class; clazz = clazz.getSuperclass()) {
                    List<Field> fieldList = Arrays.asList(clazz.getDeclaredFields());
                    fieldGroup.add(fieldList);
                }
                List<Field> fieldList = new ArrayList<>();
                for (int i = (fieldGroup.size() - 1); i >= 0; i--) {
                    fieldList.addAll(fieldGroup.get(i));
                }
                Field[] result = new Field[fieldList.size()];
                fields = fieldList.toArray(result);
            }
            Map<String, String> titleAndValueMap = new HashMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
                ExportField exportField = field.getAnnotation(ExportField.class);
                if (null == exportField) {
                    continue;
                }
                Object objValue = field.get(t) == null ? "" : field.get(t);
                titleAndValueMap.put(exportField.name(), objValue.toString());
            }
            for (String title : titles) {
                String value = titleAndValueMap.get(title);
                value = null == value ? "" : value;
                line.add(value);
            }
            lines.add(line);
        }
        return lines;
    }
}
