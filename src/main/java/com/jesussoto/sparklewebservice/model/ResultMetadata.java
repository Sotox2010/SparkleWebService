package com.jesussoto.sparklewebservice.model;

public class ResultMetadata {

    long timestamp;
    String filename;
    String url;
    long size ;

    public ResultMetadata(long timestamp, String filename, String url, long size) {
        this.timestamp = timestamp;
        this.filename = filename;
        this.url = url;
        this.size = size;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
