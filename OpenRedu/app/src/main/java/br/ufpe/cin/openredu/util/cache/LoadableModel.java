package br.ufpe.cin.openredu.util.cache;

import java.io.Serializable;

public abstract class LoadableModel implements Serializable {
	
	private static final long serialVersionUID = -3718504665853260737L;

	protected static final String DEFAULT_ENCODING = "utf-8";
	
	public String url;
	public String encoding;
	public long cacheDuration; //In milliseconds
	
	protected LoadableModel(String _url) {
		this.url = _url;
		this.encoding = DEFAULT_ENCODING;
		this.cacheDuration = 0;
	}
	
	protected LoadableModel(String _url, String encoding) {
		this(_url);
		this.encoding = encoding;
	}
	
	protected LoadableModel(String _url, long cacheDuration) {
		this(_url);
		this.cacheDuration = cacheDuration;
	}
	
	protected LoadableModel(String _url, String encoding, long cacheDuration) {
		this(_url, encoding);
		this.cacheDuration = cacheDuration;
	}
}
