package br.com.developer.redu.models;

import java.util.List;

public class Chat {
	public String id;
	public String created_at;
	public String updated_at;
	
	public List<Link> links;
	
	@Override
	public String toString(){
		return String.format("id: %s\ncreated_at: %s\nupdated_at: %s\nlinks: %s",
				id, created_at, updated_at, links);
	}
}
