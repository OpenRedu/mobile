package br.com.developer.redu.models;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 9/5/12
 * Time: 9:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class SubjectPayload {
    in subject;

    public SubjectPayload(String name, String description){
        this.subject = new in(name, description);
    }

    class in{
        String name;
        String description;

        public in(String name, String description){
            this.name = name;
            this.description = description;
        }
    }
}
