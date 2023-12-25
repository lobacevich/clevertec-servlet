package by.clevertec.lobacevich.util;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlReader {

    private static final String PATH = "application.yml";

    private YamlReader() {
    }

    public static Map<String, String> getData() {
        InputStream in = YamlReader.class.getClassLoader().getResourceAsStream(PATH);
        Yaml yaml = new Yaml();
        return yaml.load(in);
    }
}
