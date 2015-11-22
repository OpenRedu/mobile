package br.com.developer.redu.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 8/31/12
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class Environment implements Serializable {
	
	private static final long serialVersionUID = 4470256021454001735L;
	
	public String id;
	public String name;
    public String created_at;
    public String initials;
    public String path;
    public String courses_count;
    public List<Thumbnail> thumbnails;

    public List<Link> links;

    public String getThumbnailUrl() {
    	return thumbnails.get(thumbnails.size() - 1).href;
    }
    
    @Override
    public String toString(){
        return String.format("name: %s\ncreated_at: %s\ninitials: %s\npath: %s\ncourse_count: %s\nlinks: %s",
                name, created_at,initials, path,courses_count, links);
    }

}
