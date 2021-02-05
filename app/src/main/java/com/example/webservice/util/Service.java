package com.example.webservice.util;

public class Service {

    public interface ServiceListener{

            public void onSucess(Object response);

            public void onConnectionError();

    }

}
