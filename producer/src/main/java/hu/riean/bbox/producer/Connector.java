/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.producer;

import hu.riean.bbox.common.Config;
import hu.riean.bbox.common.sock.SOAPManager;
import hu.riean.bbox.common.sock.SocketHandler;
import hu.riean.bbox.farmer.model.BuyFruitRequest;
import hu.riean.bbox.farmer.model.GetGUIDRequest;
import hu.riean.bbox.farmer.model.GetGUIDResponse;
import hu.riean.bbox.farmer.model.ListFruitsRequest;
import hu.riean.bbox.farmer.model.Ping;
import hu.riean.bbox.farmer.model.Pong;
import hu.riean.bbox.producer.granary.Granary;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author riean
 */
@Service
public class Connector {

    Logger logger = LoggerFactory.getLogger(Connector.class);

    SOAPManager manager;

    @Autowired
    Granary granary;

    public Connector(SOAPManager manager) {
        this.manager = manager;
    }

    public void connect() {

        try {
            SSLSocket s = createSocket();
            SocketHandler socketHandler = manager.getSocketHandler(s);
            socketHandler.put(new GetGUIDRequest());
            Object message = socketHandler.get();

            String guid;
            if (message instanceof GetGUIDResponse GUIDResponse) {
                guid = GUIDResponse.getGuid();
                logger.info("GUID is: " + guid);
            } else {
                return;
            }
            while (s.isConnected()) {
                message = socketHandler.get();
                if (message instanceof ListFruitsRequest) {
                    logger.info("list fuits");
                    socketHandler.put(granary.listFruits());
                }
                if (message instanceof BuyFruitRequest buyFruitRequest) {
                    logger.info("buy fuit");
                    socketHandler.put(granary.buyFruit(buyFruitRequest));
                }
                if (message instanceof Ping) {
                    logger.info("list fuits");
                    socketHandler.put(new Pong());
                }
            }
        } catch (Exception e) {
            logger.error("error", e);
        }

    }

    private SSLSocket createSocket() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, IOException, KeyManagementException {
        logger.info("Opening cert");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream stream = Config.class.getClassLoader().getResourceAsStream(Config.CACERTS_FILE_NAME)) {
            keyStore.load(stream, Config.PASSWORD);
        }
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, Config.PASSWORD);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        logger.info("Setup SSL connection");
        SSLContext sc = SSLContext.getInstance(Config.PROTOCOL);
        //sc.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        sc.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        return (SSLSocket) sc.getSocketFactory().createSocket(Config.HOST, Config.PORT);
    }

}
