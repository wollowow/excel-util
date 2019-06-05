package com.angla.plugins.excel.inventor.parse;

import com.angla.plugins.excel.commons.bean.InventorBeanTemplate;
import com.angla.plugins.excel.commons.bean.InventoryCheckResult;
import com.angla.plugins.excel.commons.enums.CheckRuleEnum;
import com.angla.plugins.excel.commons.throwable.ExcelException;
import com.angla.plugins.excel.commons.throwable.exception.AnnotationException;
import com.angla.plugins.excel.inventor.anno.CustomCheckRule;
import com.angla.plugins.excel.inventor.anno.InventorField;
import com.angla.plugins.excel.inventor.anno.InventorFieldBean;
import com.angla.plugins.excel.inventor.format.CellValueFormater;
import com.angla.plugins.excel.inventor.processer.InventorAnnoProcessor;
import com.angla.plugins.excel.inventor.processer.impl.FormatCheckProcessor;
import com.angla.plugins.excel.inventor.processer.impl.RegexCheckProcessor;
import com.angla.plugins.excel.inventor.processer.impl.RequiredCheckProcessor;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title:AbstractInventor
 *
 * @author angla
 **/

public abstract class AbstractInventor<T extends InventorBeanTemplate> implements Inventor<T>{

    public AbstractInventor(Class<T> clazz) {
        buildNameAndField();
        try {
            this.clazz = clazz;
            t = clazz.newInstance();
        } catch (Exception e) {
            throw new ExcelException("初始化构造器失败：",e);
        }
    }

    private List<T> result;

    private List<T> errResult;

    protected T t;

    /**
     * 标题
     */
    protected List<String> titles = new ArrayList<>();

    protected CellValueFormater formater;

    protected Class<T> clazz;

    protected CheckRuleEnum checkRuleEnum;
    /**
     * 名称和属性关联
     */
    protected Map<String, Field> name2FieldMap = new HashMap<>();

    /**
     * 校验列表
     */
    protected List<InventorAnnoProcessor> processors = new ArrayList<>();

    protected InventorFieldBean inventorFieldBean;

    public List<T> getResult() {
        return result;
    }

    public List<T> getErrResult() {
        return errResult;
    }


    /**
     * 构建名称和属性关联的map
     * @throws AnnotationException
     */
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
            if(inventorField.required()){
                processors.add(new RequiredCheckProcessor());
                inventorFieldBean.setRequired(true);
            }
            if (CustomCheckRule.class != inventorField.custom() && CustomCheckRule.class.isAssignableFrom
                    (inventorField.custom())) {
                inventorFieldBean.setCustom(inventorField.custom());
            }
            if(!"".equals(inventorField.format())){
                inventorFieldBean.setFormat(inventorField.format());
                processors.add(new FormatCheckProcessor());
            }
            if(!"".equals(inventorField.regex())){
                inventorFieldBean.setRegex(inventorField.regex());
                processors.add(new RegexCheckProcessor());
            }
        }
        if (CollectionUtils.isEmpty(name2FieldMap.entrySet())) {
            throw new AnnotationException("需要为转化的属性配置@InventorField注解");
        }
    }

    /**
     * 校验格式
     * @param value
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private InventoryCheckResult doProcess(String value) throws NoSuchMethodException, IllegalAccessException,
            InstantiationException {

        InventoryCheckResult result;
        Class<? extends CustomCheckRule> customRule = inventorFieldBean.getCustom();
        if(null != customRule){
            result = customRule.newInstance().check(value,inventorFieldBean);
            if(!result.isChecked()){
                return result;
            }
        }
        if(CollectionUtils.isEmpty(processors)){
            return InventoryCheckResult.suc();
        }

        for(InventorAnnoProcessor processor: processors){
            result = processor.checked(value,inventorFieldBean);
            if(!result.isChecked()){
                return result;
            }
        }
        return InventoryCheckResult.suc();
    }
}
