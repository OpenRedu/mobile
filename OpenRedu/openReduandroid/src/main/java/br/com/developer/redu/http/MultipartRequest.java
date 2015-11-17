package br.com.developer.redu.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;


public class MultipartRequest {
	private OAuthRequest request;
	private Entity entity;
	private Object token;

	public MultipartRequest(String endpoint, String token) {
		this.request = new OAuthRequest(Verb.POST, endpoint);
		this.token = token;
		this.entity = new Entity();
	}
	
	public Response send() throws IOException{
		signRequest();
		byte[] payload = this.entity.toByteArray();
		this.request.addPayload(payload);
		
		return this.request.send();
	}
	
	private void signRequest() {
		this.request.addHeader("Authorization", String.format("OAuth %s", this.token));
	}

	public void setEntity(Entity entity) throws UnsupportedEncodingException {
		this.entity = entity;
		setContentType();
	}

	private void setContentType() {
		this.request.addHeader("Content-Type", this.entity.getContentType());
	}
}
