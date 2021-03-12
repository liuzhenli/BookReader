package com.micoredu.reader.web;

import com.google.gson.Gson;
import com.micoredu.reader.bean.BookSourceBean;

import java.util.List;

import fi.iki.elonen.NanoHTTPD;

public class ShareServer extends NanoHTTPD {

    private Callback callback;

    public ShareServer(int port, Callback callback) {
        super(port);
        this.callback = callback;
    }

    @Override
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse(new Gson().toJson(callback.getSources()));
    }

    public interface Callback {
        List<BookSourceBean> getSources();
    }
}
