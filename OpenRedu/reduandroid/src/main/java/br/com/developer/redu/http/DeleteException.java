package br.com.developer.redu.http;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 9/3/12
 * Time: 9:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class DeleteException extends RuntimeException {

	private static final long serialVersionUID = 5278010002200788932L;
	
	private int statusCode;
    public DeleteException(String error, int statusCode){
        super(error);
    }

    public int getStatusCode(){
        return this.statusCode;
    }
}
