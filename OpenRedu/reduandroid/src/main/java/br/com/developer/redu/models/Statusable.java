package br.com.developer.redu.models;

import java.io.Serializable;

public class Statusable implements Serializable {
	
	private static final long serialVersionUID = -5627069214550360440L;

	public static final String TYPE_USER = "users";
	public static final String TYPE_LECTURE = "lectures";
	public static final String TYPE_SPACE = "spaces";
	
	public String type;
	public String id;
	public String name;
	
	public boolean isTypeUser() {
    	return TYPE_USER.equals(type);
    }
	
	public boolean isTypeLecture() {
		return TYPE_LECTURE.equals(type);
	}
	
	public boolean isTypeSpace() {
		return TYPE_SPACE.equals(type);
	}
}
