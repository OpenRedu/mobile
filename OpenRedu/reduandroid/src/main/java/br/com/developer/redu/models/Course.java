package br.com.developer.redu.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 8/31/12
 * Time: 12:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class Course implements Serializable {

	private static final long serialVersionUID = 7065050353626090266L;
	
	public String id;
    public String name;
    public String path;
    public String created_at;
    public List<Link> links;

    @Override
    public String toString(){
        return String.format("id: %s\nname: %s\npath: %s\ncreated_at: %s\nlinks: %s",
                id,path, name, created_at, links);
    }
}
