package com.arcquim.system.android.transport;

import com.arcquim.gwt.shared.Record;
import com.arcquim.gwt.shared.RecordEncoderDecoder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessageParser {

    public String getMessageBody(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        JSONParser parser = new JSONParser();
        try {
            JSONObject object = (JSONObject) parser.parse(json);
            if (object == null) {
                return null;
            }
            Object payload = object.get(Record.RECORD_KEY);
            if (payload == null) {
                return null;
            }
            return (String) payload;
        }
        catch (ParseException ex) {
            return null;
        }
    }

    public Record getMessageBodyAsRecord(String json) {
        String payload = getMessageBody(json);
        if (payload == null || payload.isEmpty()) {
            return null;
        }
        try {
            return RecordEncoderDecoder.decode(payload);
        }
        catch(Exception ex) {
            return null;
        }
    }

    public String getMessageString(Record record) {
        JSONObject object = new JSONObject();
        object.put(Record.RECORD_KEY, RecordEncoderDecoder.encode(record));
        return object.toJSONString();
    }

}
