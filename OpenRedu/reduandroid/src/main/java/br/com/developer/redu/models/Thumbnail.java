package br.com.developer.redu.models;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 8/31/12
 * Time: 10:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class Thumbnail implements Serializable {

	private static final long serialVersionUID = 6356206180421522416L;
	
	public String size;
    public String href;

    @Override
    public String toString(){
        String retorno = String.format("size: %s\nhref: %s", size, href);
        return retorno;
    }
}
