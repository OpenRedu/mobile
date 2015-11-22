package br.com.developer.redu.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 8/31/12
 * Time: 10:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class User implements Serializable {

	private static final long serialVersionUID = -2706231301970000098L;
	
	public String login;
    public String mobile;
    public String email;
    public String localization;
    public int friends_count;
    public int id;
    public String first_name;
    public String last_name;
    public String birthday;
    public String birth_localization;
    public List<SocialNetwork> social_networks;
    public List<Link> links;
    public List<Thumbnail> thumbnails;

    public String getCompleteName() {
    	return new StringBuffer(first_name).append(" ").append(last_name).toString();
	}
    
    public String getThumbnailUrl() {
    	return thumbnails.get(thumbnails.size() - 1).href;
    }
    
    @Override
    public String toString(){

        String retorno = String.format("login: %s\nmobile: %s\nemail: %s\nlocalization: %s\nfriends_count: %s\nid: %s\n" +
                "name: %s %s\nbirthday: %s\nbirth_localization: %s\nsocial_networks: %s\nlinks: %s\nthumbnails: %s",
                login, mobile, email, localization, friends_count,id, first_name, last_name,birthday,
                birth_localization,social_networks,links, thumbnails);

        return retorno;
    }
}
