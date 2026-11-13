package com.movieticket.dao;

import com.movieticket.model.User;
import com.movieticket.util.XmlUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Data access for users stored in users.xml.
 */
public class UserDao {

    private final File xmlFile;

    public UserDao(File dataDir) {
        this.xmlFile = new File(dataDir, "users.xml");
    }

    public List<User> findAll() throws Exception {
        List<User> list = new ArrayList<>();
        Document doc = XmlUtil.load(xmlFile);
        NodeList nodes = doc.getElementsByTagName("user");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            User u = new User();
            u.setId(el.getAttribute("id"));
            u.setUsername(XmlUtil.getText(el, "username"));
            u.setPassword(XmlUtil.getText(el, "password"));
            u.setEmail(XmlUtil.getText(el, "email"));
            u.setRole(XmlUtil.getText(el, "role"));
            list.add(u);
        }
        return list;
    }

    public User findByUsername(String username) throws Exception {
        for (User u : findAll()) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    public boolean usernameExists(String username) throws Exception {
        return findByUsername(username) != null;
    }

    public void add(User u) throws Exception {
        Document doc = XmlUtil.load(xmlFile);
        Element root = doc.getDocumentElement();
        if (root == null) {
            root = doc.createElement("users");
            doc.appendChild(root);
        }
        Element el = doc.createElement("user");
        el.setAttribute("id", u.getId());
        XmlUtil.setText(doc, el, "username", u.getUsername());
        XmlUtil.setText(doc, el, "password", u.getPassword());
        XmlUtil.setText(doc, el, "email", u.getEmail());
        XmlUtil.setText(doc, el, "role", u.getRole());
        root.appendChild(el);
        XmlUtil.save(doc, xmlFile);
    }

    public void update(User user) throws Exception {
        Document doc = XmlUtil.load(xmlFile);
        NodeList nodes = doc.getElementsByTagName("user");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            if (element.getAttribute("id").equals(user.getId())) {
                XmlUtil.setText(doc, element, "email", user.getEmail());
                XmlUtil.setText(doc, element, "password", user.getPassword());
                XmlUtil.setText(doc, element, "role", user.getRole());
                XmlUtil.save(doc, xmlFile);
                return;
            }
        }
    }
}
