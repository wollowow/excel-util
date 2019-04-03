package com.angla.plugins.excel.analysis.verify;

import com.angla.plugins.excel.analysis.anno.manager.AnnoField;
import com.angla.plugins.excel.analysis.anno.manager.AnnoManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: excel-util
 * @description: 标题校验器
 * @author: angla
 * @create: 2018-08-02 17:11
 * @Version 1.0
 **/
public class TitleVerify {

    /**
     * @Description: 检验Must和uniqueness注解 记录其所在列 记录uniqueness信息
     * @Param: [container, titles, res]
     * @return: void
     * @Author: angla
     * @Date: 2018/8/3 14:57
     */
    public static <T> boolean verifyAndRecord(AnnoManager container, List<Object> titles) {
        AnnoField bean;
        String title;
        Set<Object> fieldNameSet = new HashSet(titles.size());
        for(int i=0;i<titles.size();i++){
            title = titles.get(i).toString().trim();    //获取标题的name值
            bean = container.getByColName(title);          //获取该name的注解内容

            //如果excel该列未能对应bean中配置，则忽略
            if(bean == null){
                continue;
            }

            //记录colNumMap
            container.setColNumMap(i, bean);
            //bean设置colNum属性
            bean.setColNum(i);

            fieldNameSet.add(bean.getFieldName());
        }

        //查看bean中所有的带注解的字段是否都能对应到excel中
        return fieldNameSet.size() == container.getFieldNameSet().size() && fieldNameSet.containsAll(container.getFieldNameSet());
    }
}