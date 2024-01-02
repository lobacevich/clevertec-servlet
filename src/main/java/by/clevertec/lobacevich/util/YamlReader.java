package by.clevertec.lobacevich.util;

import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlReader {

    @Getter
    private static final Map<String, String> data;
    private static final String PATH = "application.yml";

    static {
        InputStream in = YamlReader.class.getClassLoader().getResourceAsStream(PATH);
        Yaml yaml = new Yaml();
        data = yaml.load(in);
    }

    private YamlReader() {
    }
}
