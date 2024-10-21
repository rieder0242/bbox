/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.proxy.socket;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author riean
 */
@Component
public class SocketStarter {

    @Autowired
    Listener listener;

    @PostConstruct
    public void initialize() {
        try {
            listener.createServerSocket();
            listener.listen();
        } catch (GeneralSecurityException | IOException ex) {
            Logger.getLogger(SocketStarter.class.getName()).log(Level.SEVERE, null, ex);
            throw new BeanCreationException("Can not create socket", ex);
        }
    }
}
