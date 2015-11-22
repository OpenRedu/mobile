package br.com.developer.redu.http;

import java.util.Map;

public final class ArgPair implements Map.Entry<String, String> {
    private final String key;
    private String value;

    public ArgPair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(String value) {
        String old = this.value;
        this.value = value;
        return old;
    }
    @Override
    public String toString(){
    	return String.format("(%s: %s)", key, value);
    }
}