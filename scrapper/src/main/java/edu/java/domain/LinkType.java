package edu.java.domain;

public enum LinkType {
    STACKOVERFLOW("stackoverflow.com"), GITHUB("github.com");

    private final String host;

    LinkType(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }
}
