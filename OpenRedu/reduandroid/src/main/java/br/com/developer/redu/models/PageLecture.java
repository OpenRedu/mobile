package br.com.developer.redu.models;

/**
 * thiago araujo
 * data: 05/04/2013
 * email: thiagosoara@gmail.com
 */

import java.io.Serializable;

public class PageLecture extends Lecture implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5508785376681127906L;

	public String content;
	public String raw;
	
	 @Override
		public String toString() {
			return "PageLecture [content= " +content+", raw: "+raw+"]";
		}
}
