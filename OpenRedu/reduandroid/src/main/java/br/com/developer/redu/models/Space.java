package br.com.developer.redu.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 8/31/12
 * Time: 12:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class Space implements Serializable {

	private static final long serialVersionUID = 542561126652957202L;
	
	public String id;
	public String name;
    public String created_at;
    public String description;
    public List<Link> links;

    @Override
    public String toString(){
        return String.format("name: %s\ncreated_at: %s\ndescritpion: %s\nlinks: %s",
                name, created_at,description,links);
    }

}
