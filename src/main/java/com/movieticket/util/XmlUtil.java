package com.movieticket.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Utility methods for reading and writing XML data files.
 * All writes are synchronized to avoid file corruption from concurrent requests.
 */
public final class XmlUtil {

    /** Coordinates multi-file booking and cancellation operations in this application instance. */
    public static final Object DATA_LOCK = new Object();

    private XmlUtil() {
    }

    public static Document load(File xmlFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        if (!xmlFile.exists()) {
            return builder.newDocument();
        }
        try (InputStream input = new FileInputStream(xmlFile)) {
            return builder.parse(input);
        }
    }

    public static synchronized void save(Document doc, File xmlFile) throws Exception {
        File parent = xmlFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        try (OutputStream output = new FileOutputStream(xmlFile)) {
            transformer.transform(new DOMSource(doc), new StreamResult(output));
        }
    }

    public static String getText(Element parent, String tag) {
        org.w3c.dom.NodeList list = parent.getElementsByTagName(tag);
        if (list.getLength() == 0) {
            return "";
        }
        return list.item(0).getTextContent().trim();
    }

    public static void setText(Document doc, Element parent, String tag, String value) {
        org.w3c.dom.NodeList list = parent.getElementsByTagName(tag);
        Element el;
        if (list.getLength() == 0) {
            el = doc.createElement(tag);
            parent.appendChild(el);
        } else {
            el = (Element) list.item(0);
        }
        el.setTextContent(value == null ? "" : value);
    }
}
