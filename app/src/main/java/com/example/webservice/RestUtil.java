package com.example.webservice;

import com.example.webservice.model.MensagemResposta;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestUtil {

    public static Object jsonParaObjeto(String json, Class type) {
        try {
            if (json != null) {
                JsonFactory factory = new JsonFactory();
                ObjectMapper mapper = new ObjectMapper(factory);
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                return mapper.readValue(json, type);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String objetoParaJson(Object object) {
        try {
            if (object != null) {
                JsonFactory factory = new JsonFactory();
                ObjectMapper mapper = new ObjectMapper(factory);
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                return mapper.writeValueAsString(object);
            } else {
                return new MensagemResposta(true, "Nenhum registro foi encontrado").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new MensagemResposta(true, "Falha ao serializar o objeto " + object.getClass().getName()).toString();
        }
    }
}
