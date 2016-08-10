package io.bastioncore.server

import io.bastioncore.core.BastionContext
import org.apache.commons.io.FileUtils
import org.yaml.snakeyaml.Yaml
/**
 *
 */
class ServerConfig extends HashMap{


    private static ServerConfig instance

    private ServerConfig(HashMap map){
        putAll(map)
    }

    public static ServerConfig load(){
        if( instance == null) {
            File configFile = FileUtils.getFile(new File(BastionContext.instance.etcPath), 'server.yml')
            FileReader reader = new FileReader(configFile)
            instance = new ServerConfig(new Yaml().load(reader))
            reader.close()
        }
        return instance
    }
}
