package com.angla.plugins.excel.inventor.parse;

import com.angla.plugins.excel.inventor.bean.InventorBeanTemplate;
import com.angla.plugins.excel.inventor.bean.InventorParseResult;
import com.angla.plugins.excel.inventor.bean.InventoryVerifyResult;
import com.angla.plugins.excel.commons.enums.CheckRuleEnum;
import com.angla.plugins.excel.commons.throwable.ExcelException;
import com.angla.plugins.excel.commons.throwable.exception.AnnotationException;
import com.angla.plugins.excel.inventor.anno.CustomCheckRule;
import com.angla.plugins.excel.inventor.anno.InventorFieldBean;
import com.angla.plugins.excel.inventor.format.CellValueFormater;
import com.angla.plugins.excel.inventor.processer.InventorAnnoProcessor;
import com.angla.plugins.excel.inventor.processer.impl.FormatCheckProcessor;
import com.angla.plugins.excel.inventor.processer.impl.RegexCheckProcessor;
import com.angla.plugins.excel.inventor.processer.impl.RequiredCheckProcessor;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Title:AbstractInventor
 *
 * @author angla
 **/

public abstract class AbstractInventor<T extends InventorBeanTemplate> implements Inventor<T> {

    public AbstractInventor() {
    }

    public AbstractInventor(Class<T> clazz) {
        try {
            this.clazz = clazz;
            t = clazz.newInstance();
            buildNameAndField();
        } catch (Exception e) {
            throw new ExcelException("初始化构造器失败：", e);
        }
    }

    protected T t;

    /**
     * 标题
     */
    protected List<String> titles = new LinkedList<>();

    protected CellValueFormater formater;

    protected Class<T> clazz;

    protected CheckRuleEnum checkRuleEnum;
    /**
     * 名称和属性关联
     */
    protected Map<String, InventorFieldBean> name2FieldMap = new HashMap<>();

    protected List<T> sucList = new LinkedList<>();

    protected List<List<String>> errList = new LinkedList<>();

    /**
     * 构建名称和属性关联的map
     *
     * @throws AnnotationException
     */
    private void buildNameAndField() throws AnnotationException {

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            InventorFieldBean inventorFieldBean = new InventorFieldBean(field);
            String name = inventorFieldBean.getName();
            inventorFieldBean.setFieldName(field.getName());
            inventorFieldBean.setFieldType(field.getType());
            if ("".equals(name)) {
                throw new AnnotationException("注解name不能为空");
            }
            List<InventorAnnoProcessor> processors = inventorFieldBean.getProcessors();
            if (inventorFieldBean.isRequired()) {
                processors.add(new RequiredCheckProcessor());
            }
            if (!"".equals(inventorFieldBean.getFormat())) {
                inventorFieldBean.setFormat(inventorFieldBean.getFormat());
                processors.add(new FormatCheckProcessor());
            }
            if (!"".equals(inventorFieldBean.getRegex())) {
                inventorFieldBean.setRegex(inventorFieldBean.getRegex());
                processors.add(new RegexCheckProcessor());
            }
            name2FieldMap.put(name, inventorFieldBean);
        }
        if (CollectionUtils.isEmpty(name2FieldMap.entrySet())) {
            throw new AnnotationException("需要为转化的属性配置@InventorField注解");
        }
    }

    /**
     * 校验格式
     *
     * @param value
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public InventoryVerifyResult doProcess(String value, InventorFieldBean inventorFieldBean) throws
            IllegalAccessException, InstantiationException {

        InventoryVerifyResult result;
        Class<? extends CustomCheckRule> customRule = inventorFieldBean.getCustom();

        if (null != customRule) {
            result = customRule.newInstance().check(value);
            if (!result.isVerified()) {
                return result;
            }
        }
        if (CollectionUtils.isEmpty(inventorFieldBean.getProcessors())) {
            return InventoryVerifyResult.suc();
        }
        for (InventorAnnoProcessor processor : inventorFieldBean.getProcessors()) {
            result = processor.checked(value, inventorFieldBean);
            if (!result.isVerified()) {
                return result;
            }
        }
        return InventoryVerifyResult.suc();
    }


    protected InventorParseResult<T> getResult() {
        return new InventorParseResult<>(sucList, errList, titles);
    }
}
