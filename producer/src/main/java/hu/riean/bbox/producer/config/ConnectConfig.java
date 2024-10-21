/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.producer.config;

import hu.riean.bbox.producer.Connector;
import jakarta.xml.soap.SOAPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 *
 * @author riean
 */
@Configuration
public class ConnectConfig {


    @Autowired
    Connector connector;


    @EventListener(ApplicationReadyEvent.class)
    public void connect() throws SOAPException {
        connector.connect();
    }

}
