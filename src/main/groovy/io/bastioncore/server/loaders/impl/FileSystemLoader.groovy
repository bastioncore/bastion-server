package io.bastioncore.server.loaders.impl

import io.bastioncore.core.BastionContext
import io.bastioncore.core.Configuration
import io.bastioncore.core.process.impl.BasicProcess
import io.bastioncore.server.loaders.IProcessLoader
import org.yaml.snakeyaml.Yaml

/**
 *
 */
class FileSystemLoader implements IProcessLoader {
    @Override
    void loadAll() {
        def files = new File(BastionContext.instance.etcPath+'processes').listFiles(new FNFilter())
        files.each {
            def configuration = new Configuration(new Yaml().load(new FileReader(it)))
            BasicProcess.setup(configuration)
        }
    }


    class FNFilter implements FilenameFilter {
        @Override
        boolean accept(File dir, String name) {
            return name.endsWith('.yml') && !name.startsWith('.')
        }
    }
}
