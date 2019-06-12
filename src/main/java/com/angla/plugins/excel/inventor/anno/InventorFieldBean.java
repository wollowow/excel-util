package com.angla.plugins.excel.inventor.anno;

import com.angla.plugins.excel.inventor.processer.InventorAnnoProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 注解bean
 * Title:InventorFieldBean
 *
 * @author angla
 **/
public class InventorFieldBean {

    private String name;
    private String fieldName;
    private boolean required;
    private String regex;
    private String format;
    private Class<? extends CustomCheckRule> custom;
    private Type geType;
    private Class fieldType;
    private List<InventorAnnoProcessor> processors = new ArrayList<>();

    public InventorFieldBean(Field field) {

        InventorField inventorField = field.getAnnotation(InventorField.class);
        this.name = inventorField.name();
        this.required = inventorField.required();
        this.regex = inventorField.regex();
        this.format = inventorField.format();
        this.geType = field.getGenericType();
        this.fieldType = field.getType();
        if (CustomCheckRule.class != inventorField.custom() && CustomCheckRule.class.isAssignableFrom
                (inventorField.custom())) {
            this.custom = inventorField.custom();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setProcessors(List<InventorAnnoProcessor> processors) {
        this.processors = processors;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Class<? extends CustomCheckRule> getCustom() {
        return custom;
    }

    public void setCustom(Class<? extends CustomCheckRule> custom) {
        this.custom = custom;
    }

    public Type getGeType() {
        return geType;
    }

    public void setGeType(Type geType) {
        this.geType = geType;
    }

    public Class getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class fieldType) {
        this.fieldType = fieldType;
    }

    public List<InventorAnnoProcessor> getProcessors() {
        return processors;
    }
}
