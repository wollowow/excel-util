package com.angla.plugins.excel.inventor.anno;

/**
 * 注解bean
 * Title:InventorFieldBean
 *
 * @author angla
 **/
public class InventorFieldBean {

    private String name;
    private boolean required;
    private String regex;
    private String format;
    private Class<? extends CustomCheckRule> custom;


    InventorFieldBean(InventorField inventorField) {
        this.name = inventorField.name();
        this.required = inventorField.required();
        this.regex = inventorField.regex();
        this.format = inventorField.format();
        this.custom = inventorField.custom();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
