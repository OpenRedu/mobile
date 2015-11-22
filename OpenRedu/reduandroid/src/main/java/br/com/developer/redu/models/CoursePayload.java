package br.com.developer.redu.models;

import com.google.gson.Gson;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 9/4/12
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class CoursePayload {
    private course course;

    public CoursePayload(String name, String path, String workload, String description){
        course = new course();
        course.name = name;
        course.path = path;
        course.workload = workload;
        course.description = description;
    }

    class course{
        public String name;
        public String path;
        public String workload;
        public String description;
    }
    public static void main(String[] args){
        CoursePayload cp = new CoursePayload("coisa do cao", "coisa_do_cao","50", null);
        Gson g = new Gson();
        System.out.println(g.toJson(cp));
    }

}
