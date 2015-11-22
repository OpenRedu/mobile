package br.com.developer.redu.models;

import java.util.List;

public class ChatMessage {
	public String id;
	public String created_at;
	public String updated_at;
	public String message;
	
	public List<Link> links;
	
	@Override
	public String toString(){
		return String.format("id: %s\nmessage: %s\ncreated_at: %s\nupdated_at: %s\n" +
				"links: %s", id, message, created_at, updated_at, links);
	}
}
