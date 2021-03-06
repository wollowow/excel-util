package com.angla.plugins.excel.inventor.format;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Title:DefaultCellValueFormater
 *
 * @author angla
 **/
public class DefaultCellValueFormater implements CellValueFormater {

    private static final String dateFormat = "yyyy-MM-dd hh:mm:ss";


    @Override
    public Object formatValue(String value, String type, String format) throws ParseException {
        if (null == value || "".equals(value)) {
            return null;
        }
        Object formatValue = null;

        switch (type) {
            case "class java.lang.String":
                formatValue = value;
                break;

            case "class java.lang.Integer":
                if(value.endsWith(".0")){
                    value = value.substring(0,value.indexOf("."));
                }
                formatValue = Integer.valueOf(value);
                break;

            case "class java.lang.Double":
                formatValue = Double.valueOf(value);
                break;

            case "class java.lang.Boolean":
                formatValue = Boolean.valueOf(value);
                break;

            case "class java.math.BigDecimal":
                formatValue = new BigDecimal(value);
                break;

            case "class java.lang.Long":
                formatValue = Long.valueOf(value);
                break;

            case "class java.lang.Float":
                formatValue = Float.valueOf(value);
                break;

            case "class java.util.Date":
                if (null != format && !"".equals(format)) {
                    formatValue = new SimpleDateFormat(format).parse(value);
                } else {
                    formatValue = new SimpleDateFormat(dateFormat).parse(value);
                }
                break;
        }
        return formatValue;
    }
}
