package com.arcquim.gwt.shared;

import com.arcquim.gwt.shared.Record;
import com.googlecode.gwtstreamer.client.Streamer;

public class RecordEncoderDecoder {

    public static String encode(Record record) {
        return Streamer.get().toString(record);
    }

    public static Record decode(String string) {
        return (Record) Streamer.get().fromString(string);
    }

}
