package br.com.developer.redu.models;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 9/5/12
 * Time: 8:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class EnvironmentPayload {
    in environment;

    public EnvironmentPayload(String name, String path, String initials, String description){
        this.environment = new in(name, path, initials, description);
    }

    class in{
        String name;
        String path;
        String initials;
        String description;



        public in(String name,String path,String initials,String description){
            this.name = name;
            this.path = path;
            this.initials = initials;
            this.description = description;

        }
    }
}
