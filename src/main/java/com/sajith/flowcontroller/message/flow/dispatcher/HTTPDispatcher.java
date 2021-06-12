package com.sajith.flowcontroller.message.flow.dispatcher;

import com.sajith.flowcontroller.message.flow.Message;
import com.sajith.flowcontroller.message.flow.MessageProcessor;
import com.sajith.flowcontroller.message.flow.exceptions.DispatchException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class HTTPDispatcher extends MessageProcessor implements Dispatcher {
    String url;

    @Override
    public void init(Properties properties) {
        url = properties.getProperty("URL");
       if (null == url || url.isEmpty()) {
           throw new IllegalArgumentException("URL cannot be Null or empty for HTTP Dispatcher");
       }
    }

    @Override
    public void handleMessage(Message message) {
        try {
            URL endpointUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) endpointUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.connect();

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(message.getMessagePayload().getBytes(StandardCharsets.UTF_8));

            InputStream inputStream = connection.getInputStream();

            inputStream.close();
            outputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            throw new DispatchException("Error while dispatching message to the backend", e);
        }
    }
}
