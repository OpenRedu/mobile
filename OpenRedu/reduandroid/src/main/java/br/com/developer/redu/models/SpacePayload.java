package br.com.developer.redu.models;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 9/5/12
 * Time: 8:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class SpacePayload {
    in space;

    public SpacePayload(String name, String description){
        space = new in(name, description);
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

