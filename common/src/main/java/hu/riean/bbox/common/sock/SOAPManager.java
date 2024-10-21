/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.common.sock;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author riean
 */
public class SOAPManager {

    Logger logger = LoggerFactory.getLogger(SOAPManager.class);
    Object objectFactory;

    public SOAPManager(Object objectFactory) {
        this.objectFactory = objectFactory;
    }

    public SocketHandler getSocketHandler(Socket socket) throws SocketException {
        try {
            return new SocketHandler(socket, this);
        } catch (IOException ex) {
            throw new SocketException(ex);
        }
    }

    void marchal(Object message, OutputStream os) throws SocketException {
        try {
            JAXBContext context = JAXBContext.newInstance(message.getClass());

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            // nem szép, de gyors
            os.write("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body>".getBytes());
            marshaller.marshal(message, os);
            os.write("</SOAP-ENV:Body></SOAP-ENV:Envelope>".getBytes());
        } catch (JAXBException | IOException ex) {
            throw new SocketException(ex);
        }

    }

    Object unmarchal(InputStream is) throws SocketException {

        try {
            Element element = getElementAsDOM(is);
            String tagName = element.getTagName();
            String sortName = tagName.substring(tagName.indexOf(":") + 1);
            String methodName = "create" + sortName.substring(0, 1).toUpperCase() + sortName.substring(1);
            Method method = objectFactory.getClass().getMethod(methodName, new Class[]{});

            String xml = getXMLString(element);
            logger.info("get message: \"" + xml.replaceAll("\\s+", " ") + "\"");

            JAXBContext context = JAXBContext.newInstance(method.getReturnType());
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Object unmarshald = unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes()));

            return unmarshald;
        } catch (ParserConfigurationException | SAXException | IOException | NoSuchMethodException | SecurityException | IllegalArgumentException | TransformerException | TransformerFactoryConfigurationError | JAXBException ex) {
            throw new SocketException(ex);
        }
    }

    private String getXMLString(Element element) throws TransformerConfigurationException, TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        DOMSource source = new DOMSource(element);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(baos);

        transformer.transform(source, result);

        return baos.toString();

    }

    private Element getElementAsDOM(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);

        doc.getDocumentElement().normalize();
        Element element;
        element = doc.getDocumentElement();
        element = nthChild(element, 2);
        element = nthChild(element, 1);
        return element;
    }

    private Element nthChild(Element element, int j) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeType() == 1) {
                j--;
                if (j == 0) {
                    return (Element) item;
                }
            }
        }
        return null;
    }
}
