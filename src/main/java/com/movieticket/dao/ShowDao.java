package com.movieticket.dao;

import com.movieticket.model.Show;
import com.movieticket.util.XmlUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Data access for showtimes stored in shows.xml.
 */
public class ShowDao {

    private final File xmlFile;

    public ShowDao(File dataDir) {
        this.xmlFile = new File(dataDir, "shows.xml");
    }

    public List<Show> findAll() throws Exception {
        List<Show> list = new ArrayList<>();
        Document doc = XmlUtil.load(xmlFile);
        NodeList nodes = doc.getElementsByTagName("show");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            Show s = new Show();
            s.setId(el.getAttribute("id"));
            s.setMovieId(el.getAttribute("movieId"));
            s.setTheatre(readValue(el, "theatre"));
            s.setScreen(readValue(el, "screen"));
            s.setDate(readValue(el, "date"));
            s.setTime(readValue(el, "time"));
            s.setPrice(parseDouble(readValue(el, "price"), 0.0));
            s.setTotalSeats(parseInt(readValue(el, "totalSeats"), 0));
            s.setSeatLayout(readValue(el, "seatLayout"));
            s.setRowPrices(readValue(el, "rowPrices"));
            List<String> booked = new ArrayList<>();
            String raw = readValue(el, "bookedSeats");
            if (!raw.isEmpty()) {
                for (String seat : raw.split(",")) {
                    seat = seat.trim();
                    if (!seat.isEmpty()) {
                        booked.add(seat);
                    }
                }
            }
            s.setBookedSeats(booked);
            list.add(s);
        }
        return list;
    }

    public Show findById(String id) throws Exception {
        for (Show s : findAll()) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public List<Show> findByMovie(String movieId) throws Exception {
        List<Show> result = new ArrayList<>();
        for (Show s : findAll()) {
            if (s.getMovieId().equals(movieId)) {
                result.add(s);
            }
        }
        return result;
    }

    public void updateBookedSeats(String showId, List<String> bookedSeats) throws Exception {
        Document doc = XmlUtil.load(xmlFile);
        NodeList nodes = doc.getElementsByTagName("show");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            if (el.getAttribute("id").equals(showId)) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < bookedSeats.size(); j++) {
                    if (j > 0) {
                        sb.append(",");
                    }
                    sb.append(bookedSeats.get(j));
                }
                XmlUtil.setText(doc, el, "bookedSeats", sb.toString());
                XmlUtil.save(doc, xmlFile);
                return;
            }
        }
    }

    public void add(Show s) throws Exception {
        Document doc = XmlUtil.load(xmlFile);
        Element root = doc.getDocumentElement();
        if (root == null) {
            root = doc.createElement("shows");
            doc.appendChild(root);
        }
        Element el = doc.createElement("show");
        el.setAttribute("id", s.getId());
        el.setAttribute("movieId", s.getMovieId());
        XmlUtil.setText(doc, el, "theatre", s.getTheatre());
        XmlUtil.setText(doc, el, "screen", s.getScreen());
        XmlUtil.setText(doc, el, "date", s.getDate());
        XmlUtil.setText(doc, el, "time", s.getTime());
        XmlUtil.setText(doc, el, "price", String.valueOf(s.getPrice()));
        XmlUtil.setText(doc, el, "totalSeats", String.valueOf(s.getTotalSeats()));
        XmlUtil.setText(doc, el, "seatLayout", s.getSeatLayout());
        XmlUtil.setText(doc, el, "rowPrices", s.getRowPrices());
        XmlUtil.setText(doc, el, "bookedSeats", "");
        root.appendChild(el);
        XmlUtil.save(doc, xmlFile);
    }

    public void update(Show s) throws Exception {
        Document doc = XmlUtil.load(xmlFile);
        NodeList nodes = doc.getElementsByTagName("show");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            if (el.getAttribute("id").equals(s.getId())) {
                el.setAttribute("movieId", s.getMovieId());
                XmlUtil.setText(doc, el, "theatre", s.getTheatre());
                XmlUtil.setText(doc, el, "screen", s.getScreen());
                XmlUtil.setText(doc, el, "date", s.getDate());
                XmlUtil.setText(doc, el, "time", s.getTime());
                XmlUtil.setText(doc, el, "price", String.valueOf(s.getPrice()));
                XmlUtil.setText(doc, el, "totalSeats", String.valueOf(s.getTotalSeats()));
                XmlUtil.setText(doc, el, "seatLayout", s.getSeatLayout());
                XmlUtil.setText(doc, el, "rowPrices", s.getRowPrices());
                XmlUtil.save(doc, xmlFile);
                return;
            }
        }
    }

    public void delete(String id) throws Exception {
        Document doc = XmlUtil.load(xmlFile);
        NodeList nodes = doc.getElementsByTagName("show");
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

    private static String readValue(Element element, String name) {
        String value = XmlUtil.getText(element, name);
        return value.isEmpty() ? element.getAttribute(name) : value;
    }
}
