package com.angla.plugins.excel.inventor.parse.impl;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import static org.apache.poi.xssf.usermodel.XSSFRelation.NS_SPREADSHEETML;

/**
 * Title:MyXSSFSheetXMLHandler
 *
 * @author liumenghua
 **/
public class MyXSSFSheetXMLHandler extends DefaultHandler {
    private static final POILogger logger = POILogFactory.getLogger(MyXSSFSheetXMLHandler.class);



    /**
     * These are the different kinds of cells we support.
     * We keep track of the current one between
     *  the start and end.
     */
    enum xssfDataType {
        BOOLEAN,
        ERROR,
        FORMULA,
        INLINE_STRING,
        SST_STRING,
        NUMBER,
    }

    /**
     * Table with the styles used for formatting
     */
    private Styles stylesTable;

    /**
     * Read only access to the shared strings table, for looking
     *  up (most) string cell's contents
     */
    private SharedStrings sharedStringsTable;

    /**
     * Where our text is going
     */
    private final MyXSSFSheetXMLHandler.SheetContentsHandler output;

    // Set when V start element is seen
    private boolean vIsOpen;
    // Set when F start element is seen
    private boolean fIsOpen;
    // Set when an Inline String "is" is seen
    private boolean isIsOpen;
    // Set when a header/footer element is seen
    private boolean hfIsOpen;

    //开头第一个单元格为空
    private boolean isBlankHead = false;

    // Set when cell start element is seen;
    // used when cell close element is seen.
    private MyXSSFSheetXMLHandler.xssfDataType nextDataType;

    // Used to format numeric cell values.
    private short formatIndex;
    private String formatString;
    private final DataFormatter formatter;
    private int rowNum;

    private final int columnNum;
    private int nextRowNum;      // some sheets do not have rowNums, Excel can read them so we should try to handle them correctly as well

    private CellAddress preCellAddress;

    private CellAddress currentCellAddress;

    private String cellRef;
    private boolean formulasNotResults;

    // Gathers characters as they are seen.
    private StringBuilder value = new StringBuilder(64);
    private StringBuilder formula = new StringBuilder(64);
    private StringBuilder headerFooter = new StringBuilder(64);


    //列索引
    /**
     * Accepts objects needed while parsing.
     *
     * @param styles  Table of styles
     * @param strings Table of shared strings
     */
    public MyXSSFSheetXMLHandler(
            Styles styles,
            int columnNum,
            SharedStrings strings,
            MyXSSFSheetXMLHandler.SheetContentsHandler sheetContentsHandler,
            DataFormatter dataFormatter,
            boolean formulasNotResults) {
        this.stylesTable = styles;
        this.sharedStringsTable = strings;
        this.output = sheetContentsHandler;
        this.formulasNotResults = formulasNotResults;
        this.nextDataType = MyXSSFSheetXMLHandler.xssfDataType.NUMBER;
        this.formatter = dataFormatter;
        this.columnNum = columnNum;
    }

    private boolean isTextTag(String name) {
        if("v".equals(name)) {
            // Easy, normal v text tag
            return true;
        }
        if("inlineStr".equals(name)) {
            // Easy inline string
            return true;
        }
        if("t".equals(name) && isIsOpen) {
            // Inline string <is><t>...</t></is> pair
            return true;
        }
        // It isn't a text tag
        return false;
    }

    @Override
    @SuppressWarnings("unused")
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        if (uri != null && ! uri.equals(NS_SPREADSHEETML)) {
            return;
        }
        if (isTextTag(localName)) {

            vIsOpen = true;
            // Clear contents cache
            value.setLength(0);
        } else if ("is".equals(localName)) {
            // Inline string outer tag
            isIsOpen = true;
        } else if ("f".equals(localName)) {
            // Clear contents cache
            formula.setLength(0);

            // Mark us as being a formula if not already
            if(nextDataType == MyXSSFSheetXMLHandler.xssfDataType.NUMBER) {
                nextDataType = MyXSSFSheetXMLHandler.xssfDataType.FORMULA;
            }

            // Decide where to get the formula string from
            String type = attributes.getValue("t");
            if(type != null && type.equals("shared")) {
                // Is it the one that defines the shared, or uses it?
                String ref = attributes.getValue("ref");
                String si = attributes.getValue("si");

                if(ref != null) {
                    // This one defines it
                    // TODO Save it somewhere
                    fIsOpen = true;
                } else {
                    // This one uses a shared formula
                    // TODO Retrieve the shared formula and tweak it to 
                    //  match the current cell
                    if(formulasNotResults) {
                        logger.log(POILogger.WARN, "shared formulas not yet supported!");
                    } /*else {
                   // It's a shared formula, so we can't get at the formula string yet
                   // However, they don't care about the formula string, so that's ok!
                }*/
                }
            } else {
                fIsOpen = true;
            }
        }
        else if("oddHeader".equals(localName) || "evenHeader".equals(localName) ||
                "firstHeader".equals(localName) || "firstFooter".equals(localName) ||
                "oddFooter".equals(localName) || "evenFooter".equals(localName)) {
            hfIsOpen = true;
            // Clear contents cache
            headerFooter.setLength(0);
        }
        else if("row".equals(localName)) {
            String rowNumStr = attributes.getValue("r");
            if(rowNumStr != null) {
                rowNum = Integer.parseInt(rowNumStr) - 1;
            } else {
                rowNum = nextRowNum;
            }
            output.startRow(rowNum);
        }
        // c => cell
        else if ("c".equals(localName)) {
            // Set up defaults.
            this.nextDataType = MyXSSFSheetXMLHandler.xssfDataType.NUMBER;
            this.formatIndex = -1;
            this.formatString = null;
            cellRef = attributes.getValue("r");
            currentCellAddress = new CellAddress(cellRef);
            String cellType = attributes.getValue("t");
            String cellStyleStr = attributes.getValue("s");
            if ("b".equals(cellType))
                nextDataType = MyXSSFSheetXMLHandler.xssfDataType.BOOLEAN;
            else if ("e".equals(cellType))
                nextDataType = MyXSSFSheetXMLHandler.xssfDataType.ERROR;
            else if ("inlineStr".equals(cellType))
                nextDataType = MyXSSFSheetXMLHandler.xssfDataType.INLINE_STRING;
            else if ("s".equals(cellType))
                nextDataType = MyXSSFSheetXMLHandler.xssfDataType.SST_STRING;
            else if ("str".equals(cellType))
                nextDataType = MyXSSFSheetXMLHandler.xssfDataType.FORMULA;
            else if (null == cellType)
                isBlankHead = true;
            else {
                // Number, but almost certainly with a special style or format
                XSSFCellStyle style = null;
                if (stylesTable != null) {
                    if (cellStyleStr != null) {
                        int styleIndex = Integer.parseInt(cellStyleStr);
                        style = stylesTable.getStyleAt(styleIndex);
                    } else if (stylesTable.getNumCellStyles() > 0) {
                        style = stylesTable.getStyleAt(0);
                    }
                }
                if (style != null) {
                    this.formatIndex = style.getDataFormat();
                    this.formatString = style.getDataFormatString();
                    if (this.formatString == null)
                        this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if (uri != null && ! uri.equals(NS_SPREADSHEETML)) {
            return;
        }

        String thisStr = null;

        // v => contents of a cell
        if (isTextTag(localName)) {
            vIsOpen = false;

            // Process the value contents as required, now we have it all
            switch (nextDataType) {
                case BOOLEAN:
                    char first = value.charAt(0);
                    thisStr = first == '0' ? "FALSE" : "TRUE";
                    break;

                case ERROR:
                    thisStr = "ERROR:" + value;
                    break;

                case FORMULA:
                    if(formulasNotResults) {
                        thisStr = formula.toString();
                    } else {
                        String fv = value.toString();

                        if (this.formatString != null) {
                            try {
                                // Try to use the value as a formattable number
                                double d = Double.parseDouble(fv);
                                thisStr = formatter.formatRawCellContents(d, this.formatIndex, this.formatString);
                            } catch(NumberFormatException e) {
                                // Formula is a String result not a Numeric one
                                thisStr = fv;
                            }
                        } else {
                            // No formatting applied, just do raw value in all cases
                            thisStr = fv;
                        }
                    }
                    break;

                case INLINE_STRING:
                    // TODO: Can these ever have formatting on them?
                    XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                    thisStr = rtsi.toString();
                    break;

                case SST_STRING:
                    String sstIndex = value.toString();
                    try {
                        int idx = Integer.parseInt(sstIndex);
                        RichTextString rtss = sharedStringsTable.getItemAt(idx);
                        thisStr = rtss.toString();
                    }
                    catch (NumberFormatException ex) {
                        logger.log(POILogger.ERROR, "Failed to parse SST index '" + sstIndex, ex);
                    }
                    break;

                case NUMBER:
                    String n = value.toString();
                    if (this.formatString != null && n.length() > 0)
                        thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);
                    else
                        thisStr = n;
                    break;

                default:
                    thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
                    break;
            }

            // Output
            output.cell(cellRef, thisStr, null);
        } else if ("f".equals(localName)) {
            fIsOpen = false;
        } else if ("is".equals(localName)) {
            isIsOpen = false;
        } else if ("row".equals(localName)) {
            // Handle any "missing" cells which had comments attached

            // Finish up the row
            output.endRow(rowNum);

            // some sheets do not have rowNum set in the XML, Excel can read them so we should try to read them as well
            nextRowNum = rowNum + 1;
        } else if ("sheetData".equals(localName)) {
            // 补充最后一个单元格
            if(currentCellAddress.getColumn() < (columnNum -1)){
                output.cell(new CellAddress(currentCellAddress.getRow(),(columnNum -1)).formatAsString(),null,null);
            }
            // indicate that this sheet is now done
            output.endSheet();
        }
        else if("oddHeader".equals(localName) || "evenHeader".equals(localName) ||
                "firstHeader".equals(localName)) {
            hfIsOpen = false;
            output.headerFooter(headerFooter.toString(), true, localName);
        }
        else if("oddFooter".equals(localName) || "evenFooter".equals(localName) ||
                "firstFooter".equals(localName)) {
            hfIsOpen = false;
            output.headerFooter(headerFooter.toString(), false, localName);
        }else if ("c".equals(localName)){
            if(null != preCellAddress){
                //补充开头为空的单元格
                if(isBlankHead){
                    isBlankHead =false;
                    output.cell(currentCellAddress.formatAsString(),null,null);
                }else {
                    int endIndex;
                    int rowNum;
                    //跨行,因为第一个单元格不会为空，所以最多跨一行
                    if(this.currentCellAddress.getRow() - this.preCellAddress.getRow() > 0){
                        endIndex = this.columnNum - 1;
                        rowNum = preCellAddress.getRow();
                    }else {
                        endIndex = currentCellAddress.getColumn() - 1;
                        rowNum = currentCellAddress.getRow();
                    }
                    for (int i = preCellAddress.getColumn()+1; i <= endIndex; i++) {
                        output.cell(new CellAddress(rowNum,i).formatAsString(),null,null);
                    }
                }
            }
            preCellAddress = currentCellAddress;
        }
    }

    /**
     * Captures characters only if a suitable element is open.
     * Originally was just "v"; extended for inlineStr also.
     */
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (vIsOpen) {
            value.append(ch, start, length);
        }
        if (fIsOpen) {
            formula.append(ch, start, length);
        }
        if (hfIsOpen) {
            headerFooter.append(ch, start, length);
        }
    }


    /**
     * You need to implement this to handle the results
     *  of the sheet parsing.
     */
    public interface SheetContentsHandler {
        /** A row with the (zero based) row number has started */
        void startRow(int rowNum);

        /** A row with the (zero based) row number has ended */
        void endRow(int rowNum);

        /**
         * A cell, with the given formatted value (may be null),
         * and possibly a comment (may be null), was encountered.
         *
         * Sheets that have missing or empty cells may result in
         * sparse calls to <code>cell</code>. See the code in
         * <code>src/examples/src/org/apache/poi/xssf/eventusermodel/XLSX2CSV.java</code>
         * for an example of how to handle this scenario.
         */
        void cell(String cellReference, String formattedValue, XSSFComment comment);

        /** A header or footer has been encountered */
        default void headerFooter(String text, boolean isHeader, String tagName) {}

        /** Signal that the end of a sheet was been reached */
        default void endSheet() {}
    }
}

