package com.angla.demo.excel;

import com.angla.plugins.excel.analysis.anno.AnalysisField;
import com.angla.plugins.excel.commons.constant.RegexConstant;
import com.angla.plugins.excel.export.anno.ExportField;

/**
 * 导出错误数据信息
 * Title:ExportBeanError
 *
 * @author angla
 **/
public class ExportBeanError {
    @AnalysisField(name="序号",uniNos = {1})
    @ExportField(name = "序号")
    String id;
    @ExportField(name="名称")
    @AnalysisField(uniNos = {2,3},name="名称")
    String name;
    @ExportField(name="类型")
    @AnalysisField(uniNos = {3}, name="类型", transform=TranType.class)
    String type;

    @ExportField(name="价格")
    @AnalysisField(name="价格", must=true, regex= RegexConstant.MONEY)
    String money;

    @ExportField(name="生成订单时间")
    @AnalysisField(name="生成订单时间", transform=TransDate.class)
    String createTime;
    @ExportField(name = "支付时间")
    @AnalysisField(name="支付时间")
    String payTime;
    @ExportField(name="错误信息")
    String errMsg;
    @ExportField(name="内容")
    String value;

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
