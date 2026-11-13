package com.movieticket.dao;

import com.movieticket.model.Movie;
import com.movieticket.util.XmlUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Data access for the movies catalogue stored in movies.xml.
 */
public class MovieDao {

    private final File xmlFile;

    public MovieDao(File dataDir) {
        this.xmlFile = new File(dataDir, "movies.xml");
    }

    public List<Movie> findAll() throws Exception {
        List<Movie> list = new ArrayList<>();
        Document doc = XmlUtil.load(xmlFile);
        NodeList nodes = doc.getElementsByTagName("movie");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            Movie m = new Movie();
            m.setId(el.getAttribute("id"));
            m.setTitle(XmlUtil.getText(el, "title"));
            m.setGenre(XmlUtil.getText(el, "genre"));
            m.setLanguage(XmlUtil.getText(el, "language"));
            m.setDuration(parseInt(XmlUtil.getText(el, "duration"), 0));
            m.setRating(parseDouble(XmlUtil.getText(el, "rating"), 0.0));
            m.setPoster(XmlUtil.getText(el, "poster"));
            m.setDescription(XmlUtil.getText(el, "description"));
            list.add(m);
        }
        return list;
    }

    public Movie findById(String id) throws Exception {
        for (Movie m : findAll()) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    public void add(Movie m) throws Exception {
        Document doc = XmlUtil.load(xmlFile);
        Element root = doc.getDocumentElement();
        if (root == null) {
            root = doc.createElement("movies");
            doc.appendChild(root);
        }
        Element el = doc.createElement("movie");
        el.setAttribute("id", m.getId());
        XmlUtil.setText(doc, el, "title", m.getTitle());
        XmlUtil.setText(doc, el, "genre", m.getGenre());
        XmlUtil.setText(doc, el, "language", m.getLanguage());
        XmlUtil.setText(doc, el, "duration", String.valueOf(m.getDuration()));
        XmlUtil.setText(doc, el, "rating", String.valueOf(m.getRating()));
        XmlUtil.setText(doc, el, "poster", m.getPoster());
        XmlUtil.setText(doc, el, "description", m.getDescription());
        root.appendChild(el);
        XmlUtil.save(doc, xmlFile);
    }

    public void update(Movie m) throws Exception {
        Document doc = XmlUtil.load(xmlFile);
        NodeList nodes = doc.getElementsByTagName("movie");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            if (el.getAttribute("id").equals(m.getId())) {
                XmlUtil.setText(doc, el, "title", m.getTitle());
                XmlUtil.setText(doc, el, "genre", m.getGenre());
                XmlUtil.setText(doc, el, "language", m.getLanguage());
                XmlUtil.setText(doc, el, "duration", String.valueOf(m.getDuration()));
                XmlUtil.setText(doc, el, "rating", String.valueOf(m.getRating()));
                XmlUtil.setText(doc, el, "poster", m.getPoster());
                XmlUtil.setText(doc, el, "description", m.getDescription());
                XmlUtil.save(doc, xmlFile);
                return;
            }
        }
    }

    public void delete(String id) throws Exception {
        Document doc = XmlUtil.load(xmlFile);
        NodeList nodes = doc.getElementsByTagName("movie");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            if (el.getAttribute("id").equals(id)) {
                el.getParentNode().removeChild(el);
                XmlUtil.save(doc, xmlFile);
                return;
            }
        }
    }

    private static int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }

    private static double parseDouble(String s, double def) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return def;
        }
    }
}
