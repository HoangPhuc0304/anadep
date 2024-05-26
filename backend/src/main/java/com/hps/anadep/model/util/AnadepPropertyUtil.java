package com.hps.anadep.model.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hps.anadep.model.anadep.Anadep;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;

@Component
public class AnadepPropertyUtil {
    public static final String FILENAME = "anadep";
    public static final String YML_FORMAT = "yml";
    public static final String YAML_FORMAT = "yaml";
    private final ObjectMapper objectMapper;

    public AnadepPropertyUtil() {
        objectMapper = new ObjectMapper(new YAMLFactory())
                .findAndRegisterModules()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Anadep readAnadepFile(File file) {
        validateFile(file);
        return readContent(file);
    }

    public String getScanPath(File file) {
        Anadep anadep = readAnadepFile(file);
        if (anadep == null || anadep.getScanner() == null || anadep.getScanner().getPath() == null) {
            return null;
        }
        return anadep.getScanner().getPath().strip();
    }

    public List<String> getIgnoreDependencies(File file) {
        Anadep anadep = readAnadepFile(file);
        if (anadep == null || anadep.getAnalyzer() == null|| anadep.getAnalyzer().getIgnore() == null) {
            return null;
        }
        return anadep.getAnalyzer().getIgnore().stream().map(String::strip).filter(StringUtils::hasText).toList();
    }

    private Anadep readContent(File file) {
        try {
            return objectMapper.readValue(file, Anadep.class);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Parse failed: %s", e.getMessage()));
        }
    }

    private void validateFile(File file) {
        try {
            String fileName = file.getName();
            String name = fileName.substring(0, fileName.lastIndexOf("."));
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1);

            if (!name.equals(FILENAME)) {
                throw new RuntimeException(String.format("File name [%s] is invalid, should be %s", name, FILENAME));
            }

            if (!extension.equals(YML_FORMAT) && !extension.equals(YAML_FORMAT)) {
                throw new RuntimeException(String.format("Format [%s] is invalid, should be [%s, %s]", extension, YML_FORMAT, YAML_FORMAT));
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Invalid file: %s", e.getMessage()));
        }
    }
}
