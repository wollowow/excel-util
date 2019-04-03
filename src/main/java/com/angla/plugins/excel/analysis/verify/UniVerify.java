package com.angla.plugins.excel.analysis.verify;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @program: excel-util
 * @description: 唯一性验证器
 * @author: angla
 * @create: 2018-08-03 15:52
 * @Version 1.0
 **/
public class UniVerify {

    Map<Integer, Set<String>> uniRecord = new HashMap<>();      //记录表格有唯一性的数值 key:唯一性序号， value 所有单元格值的集合

    Map<Integer, StringBuffer> tempRecord = new HashMap<>();    //记录excel一行数据的唯一性规则 key：唯一性序号， value 该规则的value相加的值

    //初始化一个新的唯一性验证对象
    public UniVerify init(Set<Integer> uniNos){
        for(int uniNo:uniNos) {
            uniRecord.put(uniNo, new HashSet<>());
            tempRecord.put(uniNo, new StringBuffer());
        }
        return this;
    }

    //把值加入唯一性比对中
    public boolean add(int[] uniNos, Object value){
        for(int uniNo:uniNos){
            tempRecord.get(uniNo).append(value.toString());
        }
        return true;
    }

    //每一行处理完了之后，需要记录改行数据，并清空该行临时数据
    public Integer next(){
        Integer uniNo = isUni();
        clearTemp();
        return uniNo;
    }

    //如果返回值不为Null 则证明uniNo=返回值的规则出现了问题
    private Integer isUni(){
        //判断是否唯一
        for(Integer uniNo:tempRecord.keySet()){
            //把规则号为uniNo的临时数据保存为永久，如果保存到永久失败，证明该规则重复了
            if(uniRecord.get(uniNo).contains(tempRecord.get(uniNo).toString())){
                return uniNo;
            }
        }
        //如果唯一则把该行记录到唯一性规则里
        for(Integer uniNo:tempRecord.keySet()){
            uniRecord.get(uniNo).add(tempRecord.get(uniNo).toString());
        }
        return null;
    }

    //清空临时文件
    private void clearTemp(){
        for(StringBuffer sb:tempRecord.values()){
            sb.setLength(0);
        }
    }
}