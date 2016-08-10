package io.bastioncore.server

import io.bastioncore.core.BastionContext
import io.bastioncore.core.Configuration
import io.bastioncore.core.process.impl.BasicProcess
import io.bastioncore.server.controllers.ApiController
import io.bastioncore.server.controllers.StatusController
import io.bastioncore.server.loaders.impl.FileSystemLoader
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.yaml.snakeyaml.Yaml

/**
 *
 */
@EnableAutoConfiguration
class Main {

    public static void main(String[] args){
        SpringApplication app = new SpringApplication()
        HashSet<Object> objects = new HashSet<Object>()
        objects.add(Main.class)
        objects.add(StatusController.class)
        objects.add(ApiController.class)
        app.setSources(objects)
        BastionContext.instance.applicationContext = app.run(args)
        BastionContext.instance.initializeAll(ServerConfig.load().server.id)
        new FileSystemLoader().loadAll()
    }
}
