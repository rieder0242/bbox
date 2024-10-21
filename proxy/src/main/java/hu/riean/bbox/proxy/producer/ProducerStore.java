/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.riean.bbox.proxy.producer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author riean
 */
@Service
@Scope(scopeName = "singleton")
public class ProducerStore {

    Logger logger = LoggerFactory.getLogger(ProducerStore.class);

    Map<UUID, Producer> producers = new HashMap<>();

    public void register(UUID uuid, Producer producer) {
        producers.put(uuid, producer);
    }

    public void unregister(UUID uuid) {
        producers.remove(uuid);
    }

    public Producer get(String uuid) {
        return get(UUID.fromString(uuid));
    }

    public Producer get(UUID uuid) {
        return producers.get(uuid);
    }

    public Collection<UUID> getUUIDs() {
        return producers.keySet();
    }

    @Scheduled(fixedDelay = 10000)
    public void janitor() {
        logger.trace("run janitor");
        for (Producer producer : producers.values()) {
            if (!producer.isLive()) {
                producers.remove(producer.getUuid());
                logger.info("remove producer " + producer.getUuid().toString());
            }
        }
    }
}
