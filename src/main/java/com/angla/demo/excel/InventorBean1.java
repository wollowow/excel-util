package com.angla.demo.excel;

import com.angla.plugins.excel.commons.bean.InventorBeanTemplate;
import com.angla.plugins.excel.inventor.anno.InventorField;

import java.util.Date;

/**
 * Title:InventorBean1
 *
 * @author angla
 **/
public class InventorBean1 extends InventorBeanTemplate {


    @InventorField(name = "姓名",required = true)
    private String name;

    @InventorField(name = "年龄",required = true)
    private Integer age;

    @InventorField(name = "性别",required = true)
    private String sex;

    @InventorField(name = "生日" ,format = "yyyy-MM-dd",required = true)
    private Date birth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String toString() {
        return "InventorBean1{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", birth=" + birth +
                '}';
    }
}
