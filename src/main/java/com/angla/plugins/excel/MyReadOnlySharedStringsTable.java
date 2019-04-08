package com.angla.plugins.excel;

import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.util.Removal;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.usermodel.XSSFRelation;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Title:MyReadOnlySharedStringsTable
 *
 * @author angla
 **/
public class MyReadOnlySharedStringsTable extends DefaultHandler implements SharedStrings {

    protected final boolean includePhoneticRuns;
    protected int count;
    protected int uniqueCount;
    private List<String> strings;
    private StringBuilder characters;
    private boolean tIsOpen;
    private boolean inRPh;

    public MyReadOnlySharedStringsTable(OPCPackage pkg) throws IOException, SAXException {
        this(pkg, true);
    }

    public MyReadOnlySharedStringsTable(OPCPackage pkg, boolean includePhoneticRuns) throws IOException, SAXException {
        this.includePhoneticRuns = includePhoneticRuns;
        ArrayList<PackagePart> parts = pkg.getPartsByContentType(XSSFRelation.SHARED_STRINGS.getContentType());
        if (parts.size() > 0) {
            PackagePart sstPart = (PackagePart) parts.get(0);
            this.readFrom(sstPart.getInputStream());
        }

    }

    public MyReadOnlySharedStringsTable(PackagePart part) throws IOException, SAXException {
        this(part, true);
    }

    public MyReadOnlySharedStringsTable(PackagePart part, boolean includePhoneticRuns) throws IOException,
            SAXException {
        this.includePhoneticRuns = includePhoneticRuns;
        this.readFrom(part.getInputStream());
    }

    public void readFrom(InputStream is) throws IOException, SAXException {
        PushbackInputStream pis = new PushbackInputStream(is, 1);
        int emptyTest = pis.read();
        if (emptyTest > -1) {
            pis.unread(emptyTest);
            InputSource sheetSource = new InputSource(pis);

            try {
                XMLReader sheetParser = SAXHelper.newXMLReader();
                sheetParser.setContentHandler(this);
                sheetParser.parse(sheetSource);
            } catch (ParserConfigurationException var6) {
                throw new RuntimeException("SAX parser appears to be broken - " + var6.getMessage());
            }
        }

    }

    public int getCount() {
        return this.count;
    }

    public int getUniqueCount() {
        return this.uniqueCount;
    }

    /**
     * @deprecated
     */
    @Removal(
            version = "4.2"
    )
    @Deprecated
    public String getEntryAt(int idx) {
        return (String) this.strings.get(idx);
    }

    /**
     * @deprecated
     */
    @Removal(
            version = "4.2"
    )
    @Deprecated
    public List<String> getItems() {
        return this.strings;
    }

    public RichTextString getItemAt(int idx) {
        return new XSSFRichTextString(this.getEntryAt(idx));
    }

    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        if (uri == null || uri.equals("http://schemas.openxmlformats.org/spreadsheetml/2006/main")) {
            if ("sst".equals(localName)) {
                String count = attributes.getValue("count");
                if (count != null) {
                    this.count = Integer.parseInt(count);
                }

                String uniqueCount = attributes.getValue("uniqueCount");
                if (uniqueCount != null) {
                    this.uniqueCount = Integer.parseInt(uniqueCount);
                }

                this.strings = new ArrayList(this.uniqueCount);
                this.characters = new StringBuilder(64);
            } else if ("si".equals(localName)) {
                this.characters.setLength(0);
            } else if ("t".equals(localName)) {
                this.tIsOpen = true;
            } else if ("rPh".equals(localName)) {
                this.inRPh = true;
            }

        }
    }

    public void endElement(String uri, String localName, String name) throws SAXException {
        if (uri == null || uri.equals("http://schemas.openxmlformats.org/spreadsheetml/2006/main")) {
            if ("si".equals(localName)) {
                this.strings.add(this.characters.toString());
            } else if ("t".equals(localName)) {
                this.tIsOpen = false;
            } else if ("rPh".equals(localName)) {
                this.inRPh = false;
            }

        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.tIsOpen) {
            if (!this.inRPh) {
                this.characters.append(ch, start, length);
            }
        }

    }
}
