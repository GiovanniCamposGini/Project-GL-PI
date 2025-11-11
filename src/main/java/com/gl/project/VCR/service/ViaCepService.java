
package com.gl.project.VCR.service;

import com.gl.project.VCR.entities.ViaCEPResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepService {
    private final RestTemplate restTemplate = new RestTemplate();

    public ViaCEPResponse buscarEnderecoPorCEP(String cep) throws RuntimeException {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        ViaCEPResponse viaCEPResponse = restTemplate.getForObject(url, ViaCEPResponse.class);
        if (viaCEPResponse.getCep() == null) {
            throw new RuntimeException();
        }
        return viaCEPResponse;
    }
}
