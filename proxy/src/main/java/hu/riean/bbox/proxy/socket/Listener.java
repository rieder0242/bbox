/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.proxy.socket;

import hu.riean.bbox.common.Config;
import jakarta.xml.soap.SOAPException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Kialakítja ay ssl socketet
 *
 * @author riean
 */
@Component
public class Listener {

    Logger logger = LoggerFactory.getLogger(Listener.class);

    @Autowired
    Acceptor acceptor;

    SSLServerSocket ssl;

    public void createServerSocket() throws GeneralSecurityException, IOException {
        logger.info("Opening cert");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream stream = Config.class.getClassLoader().getResourceAsStream(Config.CACERTS_FILE_NAME)) {
            keyStore.load(stream, Config.PASSWORD);
        }
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, Config.PASSWORD);

        logger.info("Setup SSL connection");
        SSLContext sc = SSLContext.getInstance(Config.PROTOCOL);

        sc.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
        ssl = (SSLServerSocket) sc.getServerSocketFactory().createServerSocket(Config.PORT, 0, InetAddress.getByName(Config.HOST));
    }

    @Async
    public void listen() throws IOException {
        logger.info("Listen SSL socket on  " + Config.HOST + ":" + Config.PORT);

        while (true) {
            try {
                final Socket accept = ssl.accept();
                logger.info("Accept connection from " + accept.getRemoteSocketAddress().toString());
                acceptor.accept(accept);
                if (ssl.isClosed()) {
                    return;
                }
            } catch (SOAPException | IOException e) {
                logger.error("socket error", e);
            }
        }

    }

}
