package com.example.webservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.net.URLEncoder;

public class MensagemResposta implements Serializable {

    private boolean error;
    private String mensagem;

    public MensagemResposta() {
    }

    public MensagemResposta(boolean error, String mensagem) {
        this.error = error;
        this.mensagem = mensagem;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public String toString() {
        try {
            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            return URLEncoder.encode(mapper.writeValueAsString(this), "UTF-8");
        }catch(Exception e){
            e.printStackTrace();
            return "{\"error\":true,\"mensagem\":\"Falha ao serializar mensagem de retorno.\"}";
        }
    }
}
