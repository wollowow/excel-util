package com.angla.plugins.excel.commons.constant;

import java.util.regex.Pattern;

/**
 * @program: excel-util
 * @description: 常用正则表达式
 * @author: angla
 * @create: 2018-08-02 15:11
 * @Version 1.0
 **/
public interface RegexConstant {
    String MONEY = "(^[1-9]([0-9]+)?(\\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\\.[0-9]([0-9])?$)";       //金钱
    String INTEGER = "(^(\\-)?\\d{n}$)";                                                            //整数
    String NON_NEGATIVE_INTEGER = "(^\\d{n}$)";                                                     //非负整数
    String FLOAT = "^(\\-|\\+)?\\d+(\\.\\d+)?$";                                                    //浮点数
    String LETTER = "(^[A-Za-z]+$)";                                                                //字母
    String LETTER_NUMBER = "(^[A-Za-z0-9]+$)";                                                      //字母数字
    String CHINESE = "(^[\\u4e00-\\u9fa5]{0,}$)";                                                   //中文
}
