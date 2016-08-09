package br.com.developer.redu.http;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import br.com.developer.redu.models.Lecture;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 8/30/12
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class HttpClient {
    protected String consumerKey;
    protected String consumerSecret;

    public HttpClient(String consumerKey, String consumerSecret){
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    public abstract void initClient(String accessToken);

    public abstract String getAuthUrl();

    public abstract String get(String url, Map.Entry<String, String>... params);

    public abstract String post(String url,Map.Entry<String, String>... params);

    public abstract String post(String url, byte[] payload, Map.Entry<String, String>... params);

    public abstract void delete(String url, Map.Entry<String, String>... params);

    public abstract void put(String url,byte[] payload,  Map.Entry<String, String>... params);

	public abstract String postMedia(String url, Lecture lecture, java.io.File file, Entry<String, String>[] params) throws IOException;
	
	public abstract String postMedia(String url, java.io.File file, Entry<String, String>[] params) throws IOException;

}

