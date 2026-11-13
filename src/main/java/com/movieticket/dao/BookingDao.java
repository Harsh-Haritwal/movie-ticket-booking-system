package com.movieticket.dao;

import com.movieticket.model.Booking;
import com.movieticket.util.XmlUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Data access for bookings stored in bookings.xml.
 */
public class BookingDao {

    private final File xmlFile;

    public BookingDao(File dataDir) {
        this.xmlFile = new File(dataDir, "bookings.xml");
    }

    public List<Booking> findAll() throws Exception {
        List<Booking> list = new ArrayList<>();
        Document doc = XmlUtil.load(xmlFile);
        NodeList nodes = doc.getElementsByTagName("booking");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            Booking b = new Booking();
            b.setId(el.getAttribute("id"));
            b.setUserId(XmlUtil.getText(el, "userId"));
            b.setUsername(XmlUtil.getText(el, "username"));
            b.setShowId(XmlUtil.getText(el, "showId"));
            List<String> seats = new ArrayList<>();
            String raw = XmlUtil.getText(el, "seats");
            if (!raw.isEmpty()) {
                for (String seat : raw.split(",")) {
                    seat = seat.trim();
                    if (!seat.isEmpty()) {
                        seats.add(seat);
                    }
                }
            }
            b.setSeats(seats);
            b.setTotalAmount(parseDouble(XmlUtil.getText(el, "totalAmount"), 0.0));
            b.setStatus(XmlUtil.getText(el, "status"));
            b.setBookingTime(XmlUtil.getText(el, "bookingTime"));
            list.add(b);
        }
        return list;
    }

    public List<Booking> findByUser(String username) throws Exception {
        List<Booking> result = new ArrayList<>();
        for (Booking b : findAll()) {
            if (b.getUsername().equalsIgnoreCase(username)) {
                result.add(b);
            }
        }
        return result;
    }

    public Booking findById(String id) throws Exception {
        for (Booking b : findAll()) {
            if (b.getId().equals(id)) {
                return b;
            }
        }
        return null;
    }

    public boolean hasBookingForShow(String showId) throws Exception {
        for (Booking booking : findAll()) {
            if (booking.getShowId().equals(showId)) {
                return true;
            }
        }
        return false;
    }

    public void add(Booking b) throws Exception {
        Document doc = XmlUtil.load(xmlFile);
        Element root = doc.getDocumentElement();
        if (root == null) {
            root = doc.createElement("bookings");
            doc.appendChild(root);
        }
        Element el = doc.createElement("booking");
        el.setAttribute("id", b.getId());
        XmlUtil.setText(doc, el, "userId", b.getUserId());
        XmlUtil.setText(doc, el, "username", b.getUsername());
        XmlUtil.setText(doc, el, "showId", b.getShowId());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.getSeats().size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(b.getSeats().get(i));
        }
        XmlUtil.setText(doc, el, "seats", sb.toString());
        XmlUtil.setText(doc, el, "totalAmount", String.valueOf(b.getTotalAmount()));
        XmlUtil.setText(doc, el, "status", b.getStatus());
        XmlUtil.setText(doc, el, "bookingTime", b.getBookingTime());
        root.appendChild(el);
        XmlUtil.save(doc, xmlFile);
    }

    public void cancel(String bookingId) throws Exception {
        Document doc = XmlUtil.load(xmlFile);
        NodeList nodes = doc.getElementsByTagName("booking");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            if (el.getAttribute("id").equals(bookingId)) {
                XmlUtil.setText(doc, el, "status", "CANCELLED");
                XmlUtil.save(doc, xmlFile);
                return;
            }
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
