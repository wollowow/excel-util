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

    private SimpleDateFormat dateFormat;

    public DefaultCellValueFormater(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DefaultCellValueFormater() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    @Override
    public Object formatValue(String value, String type) throws ParseException {
        if (null == value || "".equals(value)) {
            return null;
        }
        Object formatValue = null;

        switch (type) {
            case "class java.lang.String":
                formatValue = value;
                break;

            case "class java.lang.Integer":
                formatValue = Integer.parseInt(value);
                break;

            case "class java.lang.Double":
                formatValue = Double.parseDouble(value);
                break;

            case "class java.lang.Boolean":
                formatValue = Boolean.parseBoolean(value);
                break;

            case "class java.math.BigDecimal":
                formatValue = new BigDecimal(value);
                break;

            case "class java.lang.Long":
                formatValue = Long.parseLong(value);
                break;

            case "class java.lang.Float":
                formatValue = Float.parseFloat(value);
                break;

            case "class java.util.Date":
                if (null != dateFormat) {
                    formatValue = dateFormat.parse(value);
                }
                break;
        }
        return formatValue;
    }
}
