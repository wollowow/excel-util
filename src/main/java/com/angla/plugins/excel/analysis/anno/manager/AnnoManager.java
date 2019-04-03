package com.angla.plugins.excel.analysis.anno.manager;

import com.angla.plugins.excel.analysis.anno.AnalysisClass;
import com.angla.plugins.excel.analysis.anno.interfaces.RowVerify;
import com.angla.plugins.excel.commons.throwable.exception.AnnotationException;
import com.angla.plugins.excel.analysis.anno.AnalysisField;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @program: excel-util
 * @description: 记录用户传入bean的容器，记录bean的基本的信息
 * @author: angla
 * @create: 2018-08-02 15:11
 * @Version 1.0
 **/
public class AnnoManager {

    private Map<String, AnnoField> colNameMap;              //key: name;

    private Map<Integer, AnnoField> colNumMap;              //key: colNum;

    private Set<Object> fieldNameSet;                       //保存dbName

    private Map<Integer,Set<String>> uniNoMap = new HashMap<>();        //保存唯一性序号集合

    private Class rowVerify;                            //对于整行的验证

    private Integer length = 0;                             //容器数据长度

    private Integer maxRow;

    private Integer minRow;

    private Integer maxColumn;

    private Integer minColumn;

    //private Integer titleRow;

    //初始化容器
    public AnnoManager(Integer colSize){
        colNameMap = new HashMap<>(colSize);
        colNumMap = new HashMap<>(colSize);
        fieldNameSet = new HashSet<>(colSize);
    }

    //添加类注解
    public void addAnalysisClass(AnalysisClass excelBean){
        //this.titleRow = excelBean.titleRow();
        this.maxRow = excelBean.maxRow();
        this.minRow = excelBean.minRow();
        this.maxColumn = excelBean.maxColumn();
        this.minColumn = excelBean.minColumn();
        Class rowVerify = excelBean.rowVerify();
        if(RowVerify.class.isAssignableFrom(rowVerify) && !rowVerify.isInterface()){
            this.rowVerify = rowVerify;
        }
    }

    //容器中添加字段bean
    public void addField(String fieldName, AnalysisField explain, Class fieldType) throws AnnotationException {
        //未使用注解直接跳过
        if(explain == null){
            return;
        }

        //创建AnnoField，保存其字段名和字段类型
        AnnoField bean = new AnnoField(fieldName, fieldType);

        //AnnoField注入注解的属性
        try {
            bean.setExplain(explain);
        } catch (Exception e) {
            throw new AnnotationException(explain.getClass().getName() + "未设置无参构造方法");
        }

        //如果未设置列名，默认使用字段名
        if(StringUtils.isBlank(bean.getColName())){
            bean.setColName(fieldName);
        }

        //校验别名重复
        if(colNameMap.containsKey(bean.getColName())){
            throw new AnnotationException("字段"+bean.getFieldName()+"所使用别名"+bean.getColName()+"重复");
        }
        colNameMap.put(bean.getColName(), bean);

        //保存字段名
        fieldNameSet.add(bean.getFieldName());

        //保存唯一性序号
        if(explain.uniNos().length > 0){
            for(int i:explain.uniNos()){
                if(uniNoMap.get(i) == null)
                    uniNoMap.put(i, new HashSet(1));
                uniNoMap.get(i).add(bean.getColName());
            }
        }
        length++;
    }

    //通过别名获取bean
    public AnnoField getByColName(String name){
        return colNameMap.get(name);
    }

    //通过列获取bean
    public AnnoField getByColNum(int colNum){
        return colNumMap.get(colNum);
    }

    //获取colNumMap的key，即都有第几列需要解析
    public Set<Integer> getColNumMapKeySet(){
        return colNumMap.keySet();
    }

    //设置colNumMap
    public void setColNumMap(Integer colNum, AnnoField bean){
        colNumMap.put(colNum, bean);
    }

    //获取字段数量
    public int getLength(){
        return length;
    }

    //获取必填项的dbName
    public Set<Object> getFieldNameSet(){
        return fieldNameSet;
    }

    //获取唯一性序号
    public Map<Integer, Set<String>> getUniNoMap(){
        return uniNoMap;
    }

    //获取行的验证规则
    public Class getRowVerify() {
        return rowVerify;
    }

    public Set<String> getUniNo(int i){
        return uniNoMap.get(i);
    }

    public Integer getMaxRow() {
        return maxRow;
    }

    public Integer getMinRow() {
        return minRow;
    }

    public Integer getMaxColumn() {
        return maxColumn;
    }

    public Integer getMinColumn() {
        return minColumn;
    }

}