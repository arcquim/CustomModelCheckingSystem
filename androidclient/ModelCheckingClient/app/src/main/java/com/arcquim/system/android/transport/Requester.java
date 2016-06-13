package com.arcquim.system.android.transport;

import com.arcquim.gwt.shared.Record;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public abstract class Requester {

    private Record answer;
    private Record record;
    private ResponseReceivedHandler handler;
    private final MessageParser messageParser = new MessageParser();
    private static final String MEDIA_CONTENT_KEY = "Content-Type";
    private static final String MEDIA_CONSUME_CONTENT_KEY = "Accept";
    private static final String MEDIA_CONTENT_VALUE = "application/json";

    public enum RequestMethod {
        GET, PUT, POST,
        DELETE, HEAD
    }

    public void setResponseReceivedHandler(ResponseReceivedHandler handler) {
        this.handler = handler;
    }

    public void sendRequest(Record record) {
        final RequestMethod requestMethod = getRequestMethod();
        if (requestMethod == null) {
            return;
        }
        final String method = requestMethod.toString();
        final String path = getPath();
        if (path == null) {
            return;
        }
        this.record = record;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(path);
                    connection  = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(method);
                    connection.setRequestProperty(MEDIA_CONSUME_CONTENT_KEY, MEDIA_CONTENT_VALUE);
                    if (requestMethod == RequestMethod.POST || requestMethod == RequestMethod.PUT) {
                        connection.setRequestProperty(MEDIA_CONTENT_KEY, MEDIA_CONTENT_VALUE);
                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(getRecordAsJson(Requester.this.record).getBytes());
                        outputStream.flush();
                    }

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        if (handler != null) {
                            handler.onFailure(null);
                        }
                        return;
                    }
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                            (connection.getInputStream())));
                    String json = "", nextLine;
                    while ((nextLine = bufferedReader.readLine()) != null) {
                        json += nextLine;
                    }
                    if (handler != null) {
                        handler.onSuccess(getJsonAsRecord(json));
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            if (handler != null) {
                handler.onFailure(null);
            }
        }
    }

    public abstract String getPath();
    public abstract RequestMethod getRequestMethod();

    public void setQueryParametersString(String queryParameters) {

    }

    private String getRecordAsJson(Record record) {
        return messageParser.getMessageString(record);
    }

    private Record getJsonAsRecord(String json) {
        return messageParser.getMessageBodyAsRecord(json);
    }

}

