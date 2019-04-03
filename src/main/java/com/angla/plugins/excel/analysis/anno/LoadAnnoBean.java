package com.angla.plugins.excel.analysis.anno;

import com.angla.plugins.excel.analysis.anno.manager.AnnoManager;
import com.angla.plugins.excel.commons.throwable.exception.AnnotationException;

import java.lang.reflect.Field;

/**
 * @program: excel-util
 * @description: 解析使用注解的bean，保存到容器中并返回
 * @author: angla
 * @create: 2018-08-02 14:42
 * @Version 1.0
 **/
public class LoadAnnoBean {

    private AnnoManager container = null;

    /**
     * @Description: 读取bean,返回容器
     * @Param: [t]
     * @return: void
     * @Author: angla
     * @Date: 2018/8/2 14:44
     */
    public <T> AnnoManager load(Class<T> t) throws AnnotationException {
        //初始化容器
        container = new AnnoManager(t.getMethods().length);

        //读取类注解
        loadClassAnno(t.getAnnotation(AnalysisClass.class));

        //读取字段注解
        loadFieldsAnno(t.getDeclaredFields());

        //查看是否读取成功
        if(container.getLength() == 0){
            throw new AnnotationException(t.getName()+"未找到配置AnalysisField注解的字段");
        }
        return container;
    }

    //读取字段
    private void loadFieldsAnno(Field[] fields) throws AnnotationException {
        for(Field field:fields){
            AnalysisField explain = field.getAnnotation(AnalysisField.class);
            String fieldName = field.getName();
            Class fieldType = field.getType();
            container.addField(fieldName, explain, fieldType);
        }
    }

    //读取bean类的AnalysisClass注解，保存到container中
    private void loadClassAnno(AnalysisClass excelBean){
        if(excelBean != null){
            container.addAnalysisClass(excelBean);
        }
    }
}
