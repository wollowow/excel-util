package com.angla.plugins.excel.inventor.parse;

import java.util.List;

/**
 * Title:Inventor
 *
 * @author liumenghua
 **/
public interface Inventor<T> {

    List<T> parse() throws Exception;

}
