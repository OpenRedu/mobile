package br.com.developer.examples;

import br.com.developer.redu.DefaultReduClient;

public class PostSubject {
	private static final String CONSUMER_KEY = "YzbH0ulBcOjXSPtmhJuEHNFFf6eZGiamQeOBQhU1";
	private static final String CONSUMER_SECRET_KEY = "kUdQsrimVZqgS7u1JuCnMGvARWhmiLWcbrZKwYO8";
	private static final String PIN = "z3nzIybwECnAxefLcdl0";
	
	public static void main(String[] args) {
		System.out.println("system on");
		DefaultReduClient client = new DefaultReduClient(CONSUMER_KEY, CONSUMER_SECRET_KEY, PIN);

		String idSpace = "2166";
		String subjectName = "Testando com 4 na sala";
		String subjectDescription = "TTesting with another four people";	
		
		client.postSubject(idSpace, subjectName, subjectDescription);
		
		
		
		System.out.println("system off");
	}
}
