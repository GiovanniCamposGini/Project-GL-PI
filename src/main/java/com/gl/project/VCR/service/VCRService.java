package com.gl.project.VCR.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;


@Component
public class VCRService {
    private static final String CASSETTE_DIR = "vcr_cassettes";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void save(String cassetteName, Object response) throws IOException {
        File dir = new File("vcr_cassettes");
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new IOException("Não foi possível criar diretório: " + dir.getAbsolutePath());
            }
        }

        File file = new File(dir, cassetteName + ".json");
        System.out.println("Salvando cassete em: " + file.getAbsolutePath());

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, response);
    }

    public <T> T load(String cassetteName, Class<T> clazz) throws IOException {
        File file = new File(CASSETTE_DIR, cassetteName + ".json");
        if (!file.exists()) return null;
        return objectMapper.readValue(file, clazz);
    }
}

