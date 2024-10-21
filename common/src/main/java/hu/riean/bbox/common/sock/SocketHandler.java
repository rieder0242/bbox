/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.common.sock;

import hu.riean.bbox.common.softsax.ParseError;
import hu.riean.bbox.common.softsax.XmlSplitter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author riean
 */
public class SocketHandler {

    Socket socket;
    XmlSplitter xmlSplitter;
    OutputStream outputStream;
    SOAPManager manager;

    Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    SocketHandler(Socket socket, SOAPManager manager) throws IOException {
        this.socket = socket;
        xmlSplitter = new XmlSplitter(socket.getInputStream());
        outputStream = socket.getOutputStream();
        this.manager = manager;
    }

    public Object get() throws SocketException, SocketIsConfusedException {
        try {
            return manager.unmarchal(xmlSplitter.next());
        } catch (ParseError ex) {
            try {
                socket.close();
            } catch (IOException iOException) {
                logger.error("Close socket", iOException);
            }
            throw new SocketIsConfusedException(ex);
        }
    }

    public void put(Object message) throws SocketException {
        manager.marchal(message, outputStream);
    }
}
