/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.proxy.ws;

import hu.riean.bbox.common.sock.SocketException;
import hu.riean.bbox.common.sock.SocketIsConfusedException;
import hu.riean.bbox.farmer.model.BuyFruitRequest;
import hu.riean.bbox.farmer.model.BuyFruitResponse;
import hu.riean.bbox.farmer.model.ListFruitsRequest;
import hu.riean.bbox.farmer.model.ListFruitsResponse;
import hu.riean.bbox.farmer.model.ListProducersResponse;
import hu.riean.bbox.proxy.error.ProxyExeption;
import hu.riean.bbox.proxy.producer.ProducerStore;
import hu.riean.bbox.proxy.socket.Listener;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.WebRequest;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 *
 * @author riean
 */
@Endpoint
public class ProxyEndpoint {

    Logger logger = LoggerFactory.getLogger(Listener.class);
    private static final String NAMESPACE_URI = "http://bbox.riean.hu/farmer/model";

    @Autowired
    ProducerStore producerStore;
    @Autowired
    WebRequest request;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "listProducersRequest")
    @ResponsePayload
    public ListProducersResponse listProducers() {
        ListProducersResponse listProducersResponse = new ListProducersResponse();
        listProducersResponse.getProducers().addAll(producerStore.getUUIDs().stream().map((UUID t) -> t.toString()).toList());
        return listProducersResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "listFruitsRequest")
    @ResponsePayload
    public ListFruitsResponse listFruits(@RequestPayload ListFruitsRequest listFruitsRequest) throws ProxyExeption {
        String guid = request.getHeader("X-Producer-GUID");
        try {
            return producerStore.get(guid).listFruits(listFruitsRequest);
        } catch (Exception ex) {
            logger.error("error", ex);
            throw new ProxyExeption(ex);
        }
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "buyFruitRequest")
    @ResponsePayload
    public BuyFruitResponse buyFruit(@RequestPayload BuyFruitRequest buyFruitRequest) throws ProxyExeption {
        String guid = request.getHeader("X-Producer-GUID");
        try {
            return producerStore.get(guid).buyFruit(buyFruitRequest);
        } catch (Exception ex) {
            logger.error("error", ex);
            throw new ProxyExeption(ex);
        }
    }
}
