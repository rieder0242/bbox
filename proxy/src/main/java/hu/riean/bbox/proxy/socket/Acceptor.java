/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.proxy.socket;

import hu.riean.bbox.common.sock.SOAPManager;
import hu.riean.bbox.common.sock.SocketException;
import hu.riean.bbox.common.sock.SocketHandler;
import hu.riean.bbox.common.sock.SocketIsConfusedException;
import hu.riean.bbox.farmer.model.GetGUIDRequest;
import hu.riean.bbox.farmer.model.GetGUIDResponse;
import hu.riean.bbox.proxy.producer.Producer;
import hu.riean.bbox.proxy.producer.ProducerStore;
import jakarta.xml.soap.SOAPException;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * @author riean
 */
@Component
public class Acceptor {

    Logger logger = LoggerFactory.getLogger(Acceptor.class);

    @Autowired
    SOAPManager manager;

    @Autowired
    ProducerStore producerStore;

    @Async
    void accept(Socket socket) throws SOAPException, IOException {
        UUID uuid = UUID.randomUUID();
        try {
            SocketHandler socketHandler = manager.getSocketHandler(socket);
            Object message = socketHandler.get();
            if (!(message instanceof GetGUIDRequest)) {
                logger.error("wrong message type");
                return;

            }
            GetGUIDResponse guidResponse = new GetGUIDResponse();
            guidResponse.setGuid(uuid.toString());
            socketHandler.put(guidResponse);
            logger.info("GUID is: " + uuid.toString());
            producerStore.register(uuid, new Producer(socketHandler, uuid));

        } catch (SocketException | SocketIsConfusedException ex) {
            logger.error("socket error", ex);

        }
    }

}
