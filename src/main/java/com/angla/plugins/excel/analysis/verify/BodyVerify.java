package com.angla.plugins.excel.analysis.verify;

import com.angla.plugins.excel.analysis.external.ErrDetail;
import com.angla.plugins.excel.analysis.external.ErrDetailTypeEnum;
import com.angla.plugins.excel.analysis.anno.interfaces.FieldTranform;
import com.angla.plugins.excel.commons.throwable.ExcelException;
import com.angla.plugins.excel.commons.util.CommonUtil;
import com.angla.plugins.excel.analysis.anno.manager.AnnoField;
import com.angla.plugins.excel.analysis.anno.manager.AnnoManager;
import com.angla.plugins.excel.analysis.external.Result;
import com.angla.plugins.excel.commons.util.ExcelFilterUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @program: excel-util
 * @description: excel内容校验器
 * @author: angla
 * @create: 2018-08-03 16:45
 * @Version 1.0
 **/
public class BodyVerify {

    /**
     * @Description: excel内容校验
     * @Param: [container, lists]
     * @return: com.hshc.shc.ics.excel.analysis.external.Res
     * @Author: angla
     * @Date: 2018/8/3 18:04
     */
    public static <Bean> Result<Bean> verifyAndRecord(AnnoManager container, List<List<Object>> lists, Class<Bean> t) throws ExcelException {
        //返回数据的封装类
        Result res = Result.suc(lists.size()/4);

        //获取类的验证方法
        Class rowVerify = container.getRowVerify();
        Object rule = null;
        Method beforeVerify = null;
        Method afterVerify = null;
        if (rowVerify != null) {
            try {
                rule = rowVerify.newInstance();
                beforeVerify = rowVerify.getMethod("beforeVerify", List.class);
                afterVerify = rowVerify.getMethod("afterVerify", t);
            } catch (InstantiationException e) {
                throw new ExcelException(rowVerify.getName() + "实例化异常");
            } catch (IllegalAccessException e) {
                throw new ExcelException(rowVerify.getName() + "实例化异常");
            } catch (NoSuchMethodException e) {
                throw new ExcelException(rowVerify.getName() + "的beforeVerify(List)或afterVerify(T)方法未找到");
            }
        }

        //验证唯一性的对象
        UniVerify uni = new UniVerify().init(container.getUniNoMap().keySet());

        //获取colNumMap的key集合，colNum
        Set<Integer> colNumMapKeySet = container.getColNumMapKeySet();

        AnnoField annoField;
        Bean bean;
        Method method;
        String dbName;
        Class tranformClass;
        Object tranformVal;

        //逐行校验
        try {
            for (int i = 0; i < lists.size(); i++) {
                List<Object> row = lists.get(i);

                //前验证
                if (rowVerify != null) {
                    ErrDetail errDetail = (ErrDetail)beforeVerify.invoke(rule, row);
                    if(errDetail != null){
                        errDetail.setErrType(ErrDetailTypeEnum.USER_DEFINED);
                        errDetail.setRow(i+1);
                        res.addErrDetail(errDetail);
                        res.addErrorList(row);
                        res.addErrorListAndMsg(row, errDetail);
                        continue;
                    }
                }

                bean = t.newInstance();                                     //获取bean的实体类

                boolean isErr = false;                                      //该行是否出错

                //获取单元格
                for (Integer colNum : colNumMapKeySet) {                    //colNum单元格所在列
                    Object value = row.get(colNum);                         //单元格值
                    annoField = container.getByColNum(colNum);              //字段注解信息
                    dbName = annoField.getFieldName();                      //字段名

                    value = valueVerify(value);

                    boolean isEmpty = value == null || value.toString().trim().length() == 0;   //单元格是否为空

                    //如果必填项为空时候
                    if ((annoField.isMust() || annoField.getUniNos().length > 0) && isEmpty) {
                        ErrDetail detail = new ErrDetail(ErrDetailTypeEnum.MUST, "【" + annoField.getColName() + "必填字段为空", i + 1,
                                colNum + 1, annoField.getColName(), dbName);
                        res.addErrDetail(detail);
                        res.addErrorList(row);
                        res.addErrorListAndMsg(row, detail);
                        isErr = true;
                        break;
                    }

                    //判断regex 为空跳过验证
                    if (!StringUtils.isBlank(annoField.getRegex()) && !isEmpty) {
                        if (!Pattern.compile(annoField.getRegex()).matcher(value + "").matches()) {
                            ErrDetail detail = new ErrDetail(ErrDetailTypeEnum.REGEX, "【" + annoField.getColName() + "正则校验失败", i + 1,
                                    colNum + 1, annoField.getColName(), dbName);
                            res.addErrDetail(detail);
                            res.addErrorList(row);
                            res.addErrorListAndMsg(row, detail);
                            isErr = true;
                            break;
                        }
                    }

                    //判断都通过后注入到bean中
                    try {
                        tranformClass = annoField.getTranform();

                        method = t.getMethod("set" + dbName.substring(0, 1).toUpperCase() + (dbName.length() > 1 ? dbName.substring(1) : ""), annoField.getFieldType());
                        if (tranformClass == FieldTranform.class) {       //如果不需要转换方法，直接放入
                            method.invoke(bean, CommonUtil.tranType(value, annoField.getFieldType()));
                        } else {                      //如果需要转换方法，则转换后放入
                            tranformVal = tranformClass.getMethod("tranform", Object.class)
                                    .invoke(annoField.getTranformObj(), value);
                            method.invoke(bean, tranformVal);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ErrDetail detail = new ErrDetail(ErrDetailTypeEnum.FORMAT, "【" + annoField.getColName() + "】类型转换错误", i + 1,
                                colNum + 1, annoField.getColName(), dbName);
                        res.addErrDetail(detail);
                        res.addErrorList(row);
                        res.addErrorListAndMsg(row, detail);
                        isErr = true;
                        break;
                    }

                    //唯一性加入对比中
                    uni.add(annoField.getUniNos(), value);
                }
                if (isErr) continue;

                //验证唯一性是否重复,重复则记录错误数据
                Integer uniNo = uni.next();     //对比该行
                if (uniNo != null) {
                    String uniFieldName = "【" + container.getUniNo(uniNo).stream().collect(Collectors.joining(",")) + "】";
                    ErrDetail detail = new ErrDetail(ErrDetailTypeEnum.UNI, uniFieldName + "字段组合不能重复", i + 1,
                            null, null, null);
                    res.addErrorList(row);
                    res.addErrDetail(detail);
                    res.addErrorListAndMsg(row, detail);
                    continue;
                }

                //行的后验证验证
                if(rowVerify!=null){
                    ErrDetail errDetail = (ErrDetail)afterVerify.invoke(rule, bean);
                    if(errDetail != null){
                        errDetail.setErrType(ErrDetailTypeEnum.USER_DEFINED);
                        errDetail.setRow(i);
                        res.addErrDetail(errDetail);
                        res.addErrorList(row);
                        res.addErrorListAndMsg(row, errDetail);
                        continue;
                    }
                }

                //没有问题 该bean注入成功的list里
                res.addSuccessList(bean);
            }
        } catch (InstantiationException e) {
            throw new ExcelException(t.getName() + "实例化异常");
        } catch (IllegalAccessException e) {
            throw new ExcelException(t.getName() + "实例化异常");
        } catch (InvocationTargetException e) {
            throw new ExcelException(rowVerify.getName() + "的beforVerify或afterVerify未找到");
        }
        return res;
    }

    private <Bean> ErrDetail rowVerify(Method verifyMethod, Object rule, Bean bean) throws ExcelException {
        ErrDetail errDetail;
        try {
            errDetail = (ErrDetail) verifyMethod.invoke(rule, bean);
        } catch (Exception e) {
            throw new ExcelException(rule.getClass()+"的"+verifyMethod.getName()+"未找到");
        }
        if (errDetail != null) {
            errDetail.setErrType(ErrDetailTypeEnum.USER_DEFINED);
        }
        return errDetail;
    }

    //对单元格内容的附加校验
    private static Object valueVerify(Object value){
        //过滤特殊字符
        if(value instanceof String){
            return ExcelFilterUtil.replaceWrongUnicode(value.toString());
        }
        return value;
    }
}
