package br.com.developer.redu.models;

public class FolderPayload {

    in folder;

    public FolderPayload(String name){
        this.folder = new in(name);
    }

    class in{
        String name;

        public in(String name){
            this.name = name;
        }
    }
}
