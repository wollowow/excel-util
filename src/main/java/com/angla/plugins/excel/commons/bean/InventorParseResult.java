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
     * 错误数据列表
     */
    private List<T> errList;

    /**
     * 处理结果 ：
     *     ALL_SUCCESS(), //全部成功
     *     ALL_FAIL(), //全部失败
     *     PARTIAL_FAILURE(); //部分失败
     */
    private ParseResultEnum parseResultEnum;

    public InventorParseResult(List<T> sucList, List<T> errList) {
        this.errList = errList;
        this.sucList = sucList;
        if (CollectionUtils.isEmpty(errList)) {
            this.parseResultEnum = ParseResultEnum.ALL_SUCCESS;
        } else if (CollectionUtils.isEmpty(sucList)) {
            this.parseResultEnum = ParseResultEnum.ALL_FAIL;
        } else {
            this.parseResultEnum = ParseResultEnum.PARTIAL_FAILURE;
        }
    }
    public List<T> getSucList() {
        return sucList;
    }

    public List<T> getErrList() {
        return errList;
    }

    public ParseResultEnum getParseResultEnum() {
        return parseResultEnum;
    }
}
