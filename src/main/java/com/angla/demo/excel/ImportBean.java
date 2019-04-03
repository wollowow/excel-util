
package com.angla.demo.excel;

import com.angla.plugins.excel.analysis.anno.AnalysisClass;
import com.angla.plugins.excel.analysis.anno.AnalysisField;
import com.angla.plugins.excel.commons.constant.RegexConstant;
import com.angla.plugins.excel.export.anno.ExportField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: excel-util
 * @description: 测试实体类
 * @author: angla
 * @create: 2018-08-01 18:01
 * @Version 1.0
 **/
@AnalysisClass(rowVerify=VerifyRow.class)
public class ImportBean {

    @AnalysisField(name="序号")
    Long id;

    @AnalysisField(uniNos = {2,3},name="名称")
    String name;

    @AnalysisField(uniNos = {3}, name="类型", transform=TranType.class)
    Integer type;

    @ExportField(scale = 3)
    @AnalysisField(name="价格", must=true, regex= RegexConstant.MONEY)
    BigDecimal money;

    @ExportField(format = "yyyy-MM-dd")
    @AnalysisField(name="生成订单时间", transform=TransDate.class)
    Date createTime;

    @AnalysisField(name="支付时间")
    Date payTime;

    ImportChild child;

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
    public void setMoney(BigDecimal money) {

        this.money = money;
    }
    public ImportChild getChild(){
        return child;
    }
    public void setChild(ImportChild child){
        this.child = child;
    }
}