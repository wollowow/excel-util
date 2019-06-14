package com.angla.plugins.excel.commons.bean;

import com.angla.plugins.excel.commons.enums.ParseResultEnum;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * Title:InventorParseResult
 *
 * @author liumenghua
 **/
public class InventorParseResult<T> {

    private List<T> sucList;
    private List<T> errList;
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
