package br.com.developer.redu.models;

import java.io.Serializable;
import java.util.List;


/**
 * User: rgcrocha
 * Date: 02/02/13
 * Time: 10:49 AM
 */

public class Lecture implements Serializable {
    
	private static final long serialVersionUID = 26909523239548218L;
	
	public final static String TYPE_CANVAS = "Canvas";
	public final static String TYPE_DOCUMENT = "Document";
	public final static String TYPE_EXERCISE = "Exercise";
	public final static String TYPE_PAGE = "Page";
	public final static String TYPE_MEDIA = "Media";
	
	public String name;
    public int position;
    public int rating;
    public String type;
    public List<Link> links;
    public int view_count;
    public String mimetype;
    public String created_at;
    public int id;
    public String updated_at;
    public String raw;
    public String content;
    
    

    @Override
	public String toString() {
		
    	String attributes = "Lecture [name=" + name + ", position=" + position + ", rating="
				+ rating + ", type=" + type + ", links=" + links
				+ ", view_count=" + view_count + ", mimetype=" + mimetype
				+ ", created_at=" + created_at + ", id=" + id + ", updated_at="
				+ updated_at + "]";
    	for (Link link : links) {
			attributes += "Rel: "+link.rel+" Href: "+link.href;
		}
    	return attributes;
	}
    
    public String getFilePath(){
    	String url = null;
		for (Link l : this.links) {
			if (l.rel.equals("raw"))
				url = l.href;
		}
		return url;
    }
    
    public String getFileName(){
    	String[] temp = getFilePath().split("\\?")[0].split("/");
    	return temp[temp.length-1];
    }
    
    public String getSelfLink(){
    	String url = null;
		for (Link l : this.links) {
			if (l.rel.equals("self_link"))
				url = l.href;
		}
		return url;
    }
}
