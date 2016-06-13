package com.arcquim.gwt.client.transport;

import com.arcquim.gwt.shared.Record;
import com.arcquim.gwt.shared.RecordEncoderDecoder;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public abstract class Requester {
    
    private Record answer;
    private ResponseReceivedHandler handler;
    private static final String MEDIA_CONTENT_KEY = "Content-Type";
    private static final String MEDIA_CONTENT_VALUE = "application/json";
    
    public enum RequestMethod {
        GET, PUT, POST,
        DELETE, HEAD
    }
    
    public void setResponseReceivedHandler(ResponseReceivedHandler handler) {
        this.handler = handler;
    }
    
    public void sendRequest(Record record) {
        RequestMethod requestMethod = getRequestMethod();
        Method method = null;
        switch (requestMethod) {
            case DELETE:
                method = RequestBuilder.DELETE;
                break;
                
            case GET:
                method = RequestBuilder.GET;
                break;
                
            case HEAD:
                method = RequestBuilder.HEAD;
                break;
                
            case POST:
                method = RequestBuilder.POST;
                break;
                
            case PUT:
                method = RequestBuilder.PUT;
                break;
        }
        if (method == null) {
            if (handler != null) {
                handler.onSuccess(null);
                return;
            }
            else {
                return;
            }
        }
        RequestBuilder requestBuilder = new RequestBuilder(method, getPath());
        if (method == RequestBuilder.POST || method == RequestBuilder.PUT) {
            requestBuilder.setHeader(MEDIA_CONTENT_KEY, MEDIA_CONTENT_VALUE);
            requestBuilder.setRequestData(getRecordAsJson(record));
        }
        requestBuilder.setCallback(new RequestCallback() {

            @Override
            public void onResponseReceived(Request request, Response response) {
                String responseBody = response.getText();
                answer = getJsonAsRecord(responseBody);
                if (handler != null) {
                    handler.onSuccess(answer);
                }
            }

            @Override
            public void onError(Request request, Throwable exception) {
                answer = null;
                if (handler != null) {
                    handler.onFailure(answer);
                }
            }
        });
        try {
            requestBuilder.send();
        } 
        catch (RequestException ex) {
            answer = null;
            if (handler != null) {
                handler.onFailure(answer);
            }
        }
    }
    
    public abstract String getPath();
    public abstract RequestMethod getRequestMethod();
    
    public void setQueryParametersString(String queryParameters) {
        
    }
    
    private String getRecordAsJson(Record record) {
        String jsonValue = RecordEncoderDecoder.encode(record);
        String json = "{\"" + Record.RECORD_KEY + "\":\"" + jsonValue + "\"}";
        return json;
    }
    
    private Record getJsonAsRecord(String json) {
        JSONValue value = JSONParser.parseStrict(json);
        if (value == null) {
            return null;
        }
        JSONObject object = value.isObject();
        if (object == null) {
            return null;
        }
        value = object.get(Record.RECORD_KEY);
        if (value == null) {
            return null;
        }
        JSONString string = value.isString();
        if (string == null) {
            return null;
        }
        String payload = string.stringValue();
        return RecordEncoderDecoder.decode(payload);
    }
    
}
