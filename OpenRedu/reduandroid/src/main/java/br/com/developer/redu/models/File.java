package br.com.developer.redu.models;

import java.util.List;

/**
 * User: rgcrocha
 * Date: 13/02/13
 * Time: 09:00 AM
 */

public class File {
	public String id;
	public String name;
	public String mimetype;
	public String size;
	public String bytes;
    public List<Link> links;
    public String created_at;
    
	@Override
	public String toString() {
		return "File [ID=" + id + ", name=" + name + ", mimetype=" + mimetype
				+ ", size=" + size + ", bytes=" + bytes + ", links=" + links
				+ ", created_at=" + created_at + "]";
	}
	
	public String getFilePath(){
		String url = null;
		for (Link l : this.links) {
			if (l.rel.equals("raw"))
				url = l.href;
		}
		return url;
	}
}
