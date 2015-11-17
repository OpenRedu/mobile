package br.com.developer.redu.models;

import java.util.Scanner;

import br.com.developer.redu.DefaultReduClient;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 9/11/12
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class Testing {
    public static void main(String[] args){

        DefaultReduClient redu = new DefaultReduClient("yourconsumerkey", "yoursecretkey");
        Scanner in = new Scanner(System.in);
        System.out.println("Visit this url: "+redu.getAuthorizeUrl());
        System.out.println("Enter your pin:");
        String pin = in.nextLine();
        redu.initClient(pin);
        System.out.println(redu.getMe());
    }
}
