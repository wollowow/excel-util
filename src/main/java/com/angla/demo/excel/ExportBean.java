package com.angla.demo.excel;

import com.angla.plugins.excel.export.anno.CustomRule;
import com.angla.plugins.excel.export.anno.ExportField;
import com.angla.plugins.excel.export.anno.SuperInclude;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Title:ExportBean
 *
 * @author angla
 **/
@SuperInclude()
public class ExportBean{

    @ExportField(name = "序号")
    Long id;

    @ExportField(name = "名称", suffix = "先生", prefix = "姓名：")
    String name;

    @ExportField(name = "类型")
    Integer type;

    @ExportField(scale = 3)
    BigDecimal money;

    @ExportField()
    Date createTime;
    @ExportField(format = "yyyy-MM-dd:HH:mm:ss", name = "支付时间")
    Date payTime;

    @ExportField(name = "金额", percent = true,custom = CustomRule.class)
    Double value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
