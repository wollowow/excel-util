package com.angla.plugins.excel.export.anno;

import com.angla.plugins.excel.commons.throwable.exception.AnnotationException;
import com.angla.plugins.excel.commons.throwable.exception.ParameterException;
import com.angla.plugins.excel.export.processer.ExportAnnoProcessor;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 管理注解
 * Title:ExportAnnoManager
 *
 * @author angla
 **/
public class ExportAnnoManager {


    private List<String> titles = new ArrayList<>();
    private List<List<String>> datas = new ArrayList<>();
    private List<String> columns = new ArrayList<>();

    public ExportAnnoManager init(List datas, Class tclass, List<String> columns) throws Exception {
        this.columns = columns;
        loadTitles(tclass, columns);
        loadData(datas, tclass);
        return this;
    }

    public List<List<String>> getDatas() {
        return datas;
    }

    public List<String> getTitles() {
        return titles;
    }

    public List<String> getColumns() {
        return columns;
    }

    /**
     * 加载标题
     *
     * @param tClass
     * @throws AnnotationException
     */
    private void loadTitles(Class tClass, List<String> columns) throws AnnotationException,
            ParameterException {
        Field[] fields = loadFields(tClass);
        //校验给定的属性list是否正确
        if (!checkColumns(columns, fields)) {
            throw new AnnotationException("导出属性注解配置异常");
        }
        List<Field> fieldList = buildExportFields(columns, fields);
        for (Field field : fieldList) {
            String fieldName = field.getName();
            ExportField exportField = field.getAnnotation(ExportField.class);
            if (null == exportField) {
                continue;
            }
            if (!CollectionUtils.isEmpty(columns) && !columns.contains(fieldName)) {  //按照给定list进行导出
                continue;
            }
            String name = "".equals(exportField.name()) ? fieldName : exportField.name();
            titles.add(name);
        }
        if (CollectionUtils.isEmpty(titles)) {
            throw new AnnotationException("未配置注解");
        }
    }

    /**
     * 加载数据
     *
     * @param data
     * @param tClass
     * @throws Exception
     */
    private void loadData(List data, Class tClass) throws Exception {
        Field[] fields = loadFields(tClass);
        if (!checkColumns(columns, fields)) {
            throw new AnnotationException("导出属性注解配置异常");
        }
        List<Field> fieldList = buildExportFields(columns, fields);
        for (Object o : data) {
            if (null == o) {
                throw new ParameterException("导出数据不能为空");
            }
            List<String> values = new ArrayList<>();
            for (Field field : fieldList) {
                String fieldName = field.getName();
                if (!CollectionUtils.isEmpty(columns) && !columns.contains(fieldName)) {
                    continue;
                }
                ExportField exportField = field.getAnnotation(ExportField.class);
                if (null == exportField) {
                    continue;
                }
                ExportFieldBean exportFieldBean = new ExportFieldBean(exportField, fieldName);
                List<ExportAnnoProcessor> exportAnnoProcessers = exportFieldBean.getProcessors();
                field.setAccessible(true); //设置些属性是可以访问的
                Object value = field.get(o);
                values.add(doProcess(exportAnnoProcessers, value, exportFieldBean));
            }
            datas.add(values);
        }
    }

    /**
     * 根据注解规则格式化数据
     *
     * @param exportAnnoProcessers
     * @param value
     * @param exportFieldBean
     * @return
     * @throws Exception
     */
    private String doProcess(List<ExportAnnoProcessor> exportAnnoProcessers, Object value,
                             ExportFieldBean exportFieldBean) throws Exception {
        Class<? extends CustomRule> customRule = exportFieldBean.getCustomRule();
        //先处理用户自定义规则
        if (null != customRule) {
            value = customRule.newInstance().rule(value);
        }
        //处理注解规则
        for (ExportAnnoProcessor e : exportAnnoProcessers) {
            value = e.process(value, exportFieldBean);
        }
        value = value == null ? "" : value;
        return value.toString();
    }

    /**
     * 检查传入列的list是否正确
     *
     * @param columns
     * @param fields
     * @return
     */
    private boolean checkColumns(List<String> columns, Field[] fields) throws ParameterException {
        if (CollectionUtils.isEmpty(columns)) {
            return true;
        }
        Set<String> set = new HashSet<>(columns);
        //传入列名称不可重复
        if (set.size() < columns.size()) {
            throw new ParameterException("导出列不能重复");
        }
        List<String> fieldNames = new ArrayList<>();
        for (Field field : fields) {
            fieldNames.add(field.getName());
        }
        return fieldNames.containsAll(columns);
    }

    /**
     * 按照传入的list对导出属性进行排序
     *
     * @param columns
     * @param fields
     * @return
     */
    private List<Field> buildExportFields(List<String> columns, Field[] fields) {
        List<Field> fieldList = new ArrayList<>();
        //不配置columns时返回所有属性，按默认排序
        if (CollectionUtils.isEmpty(columns)) {
            return Arrays.asList(fields);
        }
        for (String column : columns) {
            for (Field field : fields) {
                if (field.getName().equals(column)) {
                    fieldList.add(field);
                }
            }
        }
        return fieldList;
    }

    /**
     * 包含父类属性
     *
     * @param tClass
     * @return
     */
    private Field[] loadFields(Class tClass) {
        Annotation superInclude = tClass.getAnnotation(SuperInclude.class);
        if (null == superInclude) {
            return tClass.getDeclaredFields();
        }
        List<List<Field>> fieldGroup = new ArrayList<>();
        for (Class<?> clazz = tClass; clazz != Object.class; clazz = clazz.getSuperclass()) {
            List<Field> fieldList = Arrays.asList(clazz.getDeclaredFields());
            fieldGroup.add(fieldList);
        }
        List<Field> fields = new ArrayList<>();
        for (int i = (fieldGroup.size() - 1); i >= 0; i--) {
            fields.addAll(fieldGroup.get(i));
        }
        Field[] result = new Field[fields.size()];
        return fields.toArray(result);
    }

}
