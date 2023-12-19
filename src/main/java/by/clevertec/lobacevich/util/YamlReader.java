package by.clevertec.lobacevich.util;

import by.clevertec.lobacevich.exception.YamlReaderException;
import lombok.Cleanup;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class YamlReader {

    private static final String PATH = "src/main/resources/application.yml";

    private YamlReader() {
    }

    public static Map<String, String> getData() {
        try {
            @Cleanup
            InputStream in = new FileInputStream(PATH);
            Yaml yaml = new Yaml();
            return yaml.load(in);
        } catch (IOException e) {
            throw new YamlReaderException("Can't read yml");
        }
    }
}
