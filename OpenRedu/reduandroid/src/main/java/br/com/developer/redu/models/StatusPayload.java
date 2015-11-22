package br.com.developer.redu.models;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 9/5/12
 * Time: 9:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class StatusPayload {
    in status;
    public StatusPayload(String text){
        this.status = new in(text);
    }
    public StatusPayload(String text, String type){
        this.status = new in(text, type);
    }

    class in{
        String text;
        String type;
        public in(String text){
            this.text = text;
        }
        public in(String text, String type){
            this.text = text;
            this.type = type;
        }
    }
}
