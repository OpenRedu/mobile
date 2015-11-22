package br.com.developer.redu.http;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 9/5/12
 * Time: 8:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class PutException extends RuntimeException {
	
	private static final long serialVersionUID = -4503703345771473064L;

	private int code;
    public PutException(String text, int code){
        super(text);
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }

}
