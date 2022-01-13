package ru.notebot.service.util;

import java.net.URI;
import java.util.StringJoiner;

public class ApiUriHelper {
    private final String appUrl;
    private final String API_PREFIX = "api";
    private final String apiVersion = "v1";

    public ApiUriHelper(String appUrl) {
        this.appUrl = appUrl;
    }

    public URI uriFrom(String...paths) {
        StringJoiner joiner = new StringJoiner("/", "/", "");
        joiner.add(API_PREFIX);
        joiner.add(apiVersion);
        for (String path : paths) {
            joiner.add(path);
        }
        String uriStr = appUrl.concat(joiner.toString());
        return URI.create(uriStr);
    }

}
