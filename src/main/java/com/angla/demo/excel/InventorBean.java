package com.angla.demo.excel;

import com.angla.plugins.excel.inventor.bean.InventorBeanTemplate;
import com.angla.plugins.excel.export.anno.ExportField;
import com.angla.plugins.excel.export.anno.SuperInclude;
import com.angla.plugins.excel.inventor.anno.InventorField;

import java.util.Date;

/**
 * Title:InventorBean1
 *
 * @author angla
 **/
@SuperInclude
public class InventorBean extends InventorBeanTemplate {


    @InventorField(name = "姓名",custom = MyVerifyRule.class)
    @ExportField(name = "姓名",suffix = "先生")
    private String name;

    @InventorField(name = "年龄")
    @ExportField(name = "年龄",scale = 2)
    private Integer age;

    @InventorField(name = "性别")
    @ExportField(name = "性别")
    private String sex;

    @InventorField(name = "生日",format = "yyyyMMdd")
    @ExportField(name = "生日",format = "yyyy-MM-dd")
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
