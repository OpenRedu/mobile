package br.com.developer.redu.models;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 9/5/12
 * Time: 8:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class EnrollmentPayload {
    in enrollment;
    public EnrollmentPayload(String email){
        enrollment = new in(email);
    }
    class in{
        public String email;
        public in(String email){
            this.email = email;

        }
    }
}
