/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.proxy.ws;

import hu.riean.bbox.common.Config;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author riean
 */
@Controller
public class WSDContoller {

    @GetMapping(value ="/farmer/model/farmer.xsd", produces  = "application/xml" )
    public @ResponseBody String getXSD() throws IOException {
        InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("farmer.xsd");
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

}
