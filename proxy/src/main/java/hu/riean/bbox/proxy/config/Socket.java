/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.proxy.config;

import hu.riean.bbox.common.sock.SOAPManager;
import hu.riean.bbox.farmer.model.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author riean
 */
@Configuration
public class Socket {

    @Bean
    public SOAPManager SOAPManager() {
        return new SOAPManager(new ObjectFactory());
    }
}
