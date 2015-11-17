package br.ufpe.cin.openredu.data;

public interface OnLoadStatusesFromWebListener {
	
	public void onStart();
	public void onComplete();
	public void onError(Exception e);

}