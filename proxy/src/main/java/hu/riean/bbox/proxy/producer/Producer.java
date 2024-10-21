/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.proxy.producer;

import hu.riean.bbox.common.sock.SocketException;
import hu.riean.bbox.common.sock.SocketHandler;
import hu.riean.bbox.common.sock.SocketIsConfusedException;
import hu.riean.bbox.farmer.model.BuyFruitRequest;
import hu.riean.bbox.farmer.model.BuyFruitResponse;
import hu.riean.bbox.farmer.model.ListFruitsRequest;
import hu.riean.bbox.farmer.model.ListFruitsResponse;
import hu.riean.bbox.farmer.model.Ping;
import hu.riean.bbox.farmer.model.Pong;
import hu.riean.bbox.proxy.socket.Listener;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author riean
 */
public class Producer {

    Logger logger = LoggerFactory.getLogger(Listener.class);
    SocketHandler socketHandler;
    UUID uuid;
    boolean live = true;

    public Producer(SocketHandler socketHandler, UUID uuid) {
        this.socketHandler = socketHandler;
        this.uuid = uuid;
    }

    public ListFruitsResponse listFruits(ListFruitsRequest listFruitsRequest) throws SocketException, SocketIsConfusedException {
        logger.info("get listFruits from " + uuid.toString());
        return call(listFruitsRequest);
    }

    public BuyFruitResponse buyFruit(BuyFruitRequest buyFruitRequest) throws SocketException, SocketIsConfusedException {
        logger.info("get listFruits from " + uuid.toString());
        return call(buyFruitRequest);
    }

    public boolean ping() {
        logger.debug("ping from " + uuid.toString());
        Pong pong;
        try {
            pong = call(new Ping());
            return pong != null;
        } catch (SocketException | SocketIsConfusedException  ex) {
            logger.debug("ping faild " + uuid.toString());
            return false;
        }

    }

    private synchronized <T> T call(Object listFruitsRequest) throws SocketException, SocketIsConfusedException {
        try {
            socketHandler.put(listFruitsRequest);
            return (T) socketHandler.get();
        } catch (SocketException | SocketIsConfusedException ex) {
            live = false;
            throw ex;
        }
    }

    boolean isLive() {
        return live;
    }

    public UUID getUuid() {
        return uuid;
    }

}
