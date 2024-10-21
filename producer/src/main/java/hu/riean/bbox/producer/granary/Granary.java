/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.producer.granary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.riean.bbox.farmer.model.BuyFruitRequest;
import hu.riean.bbox.farmer.model.BuyFruitResponse;
import hu.riean.bbox.farmer.model.ListFruitsResponse;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author riean
 */
@Service
public class Granary {

    Logger logger = LoggerFactory.getLogger(Granary.class);

    @Value("${granary.json:#{null}}")
    private String jsonFile;

    private List<Fruit> fruits = new ArrayList<>();

    @PostConstruct
    void init() {
        if (jsonFile == null) {
            logger.warn("Run widout \"granary.json\" argument, use default.");
            defaultValue();
            return;
        }
        Path path = Paths.get(jsonFile);
        if (!Files.isRegularFile(path)) {
            logger.warn("Can not found \"" + jsonFile + "\", use default.");
            defaultValue();
            return;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            fruits = mapper.readValue(Files.newInputStream(path), new TypeReference<List<Fruit>>() {
            });
        } catch (IOException ex) {
            logger.warn("Erro becouse open \"granary.json\", use default.", ex);
            defaultValue();
        }
    }

    private void defaultValue() {
        fruits.add(new Fruit().setName("apple").setPrice(699));
        fruits.add(new Fruit().setName("banan").setPrice(395));
        fruits.add(new Fruit().setName("citron").setPrice(899));
    }

    public ListFruitsResponse listFruits() {
        ListFruitsResponse producersResponse = new ListFruitsResponse();
        producersResponse.getFruit().addAll(fruits.stream().map((Fruit in) -> {
            ListFruitsResponse.Fruit out = new ListFruitsResponse.Fruit();
            out.setName(in.getName());
            out.setPrice(in.getPrice());
            return out;
        }).toList());
        return producersResponse;
    }

    public BuyFruitResponse buyFruit(BuyFruitRequest buyFruitRequest) {
        final BuyFruitResponse buyFruitResponse = new BuyFruitResponse();
        buyFruitResponse.setName(buyFruitRequest.getName());
        buyFruitResponse.setSuccess(false);
        for (Fruit fruit : fruits) {
            if (fruit.getName().equals(buyFruitRequest.getName())) {
                buyFruitResponse.setQuantity(buyFruitRequest.getQuantity());
                buyFruitResponse.setPrice(fruit.getPrice() * buyFruitRequest.getQuantity());
                buyFruitResponse.setSuccess(true);
                break;
            }
        }
        return buyFruitResponse;
    }

}
