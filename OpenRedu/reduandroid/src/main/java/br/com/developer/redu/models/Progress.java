package br.com.developer.redu.models;

import java.io.Serializable;
import java.util.List;

public class Progress implements Serializable {

	private static final long serialVersionUID = -1146426565289529613L;
	
	public String id;
	public String finalized;
	public String updated_at;
	public List<Link> links;
	
}
