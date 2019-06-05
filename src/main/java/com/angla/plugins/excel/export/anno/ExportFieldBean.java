package com.angla.plugins.excel.export.anno;

import com.angla.plugins.excel.commons.enums.ISEnum;
import com.angla.plugins.excel.export.processer.ExportAnnoProcessor;
import com.angla.plugins.excel.export.processer.impl.DateFormatProcessor;
import com.angla.plugins.excel.export.processer.impl.EnumFormatProcessor;
import com.angla.plugins.excel.export.processer.impl.PercentFormatProcessor;
import com.angla.plugins.excel.export.processer.impl.PrefixFormatProcessor;
import com.angla.plugins.excel.export.processer.impl.ScaleFormatProcessor;
import com.angla.plugins.excel.export.processer.impl.suffixFormatProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 注解bean
 * Title:ExportFieldBean
 *
 * @author angla
 **/
public class ExportFieldBean {

    private String fieldName; //属性名称
    private String name;
    private String format;
    private int scale;
    private String prefix;
    private String suffix;
    private Class<? extends CustomRule> customRule;
    private Class<? extends ISEnum> enumRule;
    private boolean percent;


    ExportFieldBean(ExportField exportField, String fieldName) {
        if (null == exportField) {
            return;
        }
        this.fieldName = fieldName;
        this.name = exportField.name();
        this.format = exportField.format();
        this.scale = exportField.scale();
        this.prefix = exportField.prefix();
        this.suffix = exportField.suffix();
        this.percent = exportField.percent();
        if (CustomRule.class != exportField.custom() && CustomRule.class.isAssignableFrom
                (exportField.custom())) {
            this.customRule = exportField.custom();
        }
        if (ISEnum.class != exportField.enumRule() && ISEnum.class.isAssignableFrom
                (exportField.enumRule())) {
            this.enumRule = exportField.enumRule();
        }
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public int getScale() {
        return scale;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public Class<? extends CustomRule> getCustomRule() {
        return customRule;
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isPercent() {
        return percent;
    }

    public Class<? extends ISEnum> getEnumRule() {
        return enumRule;
    }

    /**
     * 当前属性需要进行哪些处理
     * 先添加数字，时间类型处理器，避免对后续操作造成干扰
     *
     * @return
     */
    List<ExportAnnoProcessor> getProcessors() {
        List<ExportAnnoProcessor> exportAnnoProcessers = new ArrayList<>();
        if (null != enumRule) {
            exportAnnoProcessers.add(new EnumFormatProcessor());
        }
        if (0 < scale) {
            exportAnnoProcessers.add(new ScaleFormatProcessor());
        }
        if (percent) {
            exportAnnoProcessers.add(new PercentFormatProcessor());
        }
        if (null != format && !"".equals(format)) {
            exportAnnoProcessers.add(new DateFormatProcessor());
        }
        if (null != prefix && !"".equals(prefix)) {
            exportAnnoProcessers.add(new PrefixFormatProcessor());
        }
        if (null != suffix && !"".equals(suffix)) {
            exportAnnoProcessers.add(new suffixFormatProcessor());
        }
        return exportAnnoProcessers;
    }
}
