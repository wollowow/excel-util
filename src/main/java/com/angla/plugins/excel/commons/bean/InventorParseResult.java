package com.angla.plugins.excel.commons.bean;

import com.angla.plugins.excel.commons.enums.ParseResultEnum;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * Title:InventorParseResult
 *
 * @author angla
 **/
public class InventorParseResult<T> {

    /**
     * 正确数据列表
     */
    private List<T> sucList;
    /**
     * 格式错误数据列表
     */
    private List<List<String>> errList;

    /**
     * 标题行
     */
    private List<String> titles;

    /**
     * 处理结果 ：
     * ALL_SUCCESS(), //全部成功
     * ALL_FAIL(), //全部失败
     * PARTIAL_FAILURE(); //部分失败
     */
    private ParseResultEnum parseResultEnum;

    public InventorParseResult(List<T> sucList, List<List<String>> errList, List<String> titles) {
        this.titles = titles;
        this.errList = errList;
        this.sucList = sucList;
        if (CollectionUtils.isEmpty(errList)) {
            this.parseResultEnum = ParseResultEnum.ALL_SUCCESS;
        } else if (CollectionUtils.isEmpty(sucList)) {
            titles.add(0, "错误信息");
            this.parseResultEnum = ParseResultEnum.ALL_FAIL;
        } else {
            titles.add(0, "错误信息");
            this.parseResultEnum = ParseResultEnum.PARTIAL_FAILURE;
        }
    }

    public List<T> getSucList() {
        return sucList;
    }

    public List<List<String>> getErrList() {
        return errList;
    }

    public List<String> getTitles() {
        return titles;
    }
    public ParseResultEnum getParseResultEnum() {
        return parseResultEnum;
    }
}
