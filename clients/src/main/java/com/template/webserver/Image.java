package com.template.webserver;

import java.math.BigInteger;
import java.security.MessageDigest;
import org.apache.commons.codec.digest.DigestUtils;

public class Image {

    private  String hash;
    private final String caption;
    private final String timestamp;
    private final String location;


    public Image(String src, String caption, String timestamp, String location) {
        this.hash = createImageHash(src);
        this.caption = caption;
        this.timestamp = timestamp;
        this.location = location;
    }

    public String getCaption() {
        return caption;
    }

    public String getLocation() {
        return location;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getHash() {
        return hash;
    }

    private String createImageHash(String image) {

        return DigestUtils.sha256Hex(image);

    }
}
