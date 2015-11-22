package br.com.developer.redu.models;

public class ProgressPayload {
	in progress;

    public ProgressPayload(String finalized){
        progress = new in(finalized);
    }

    class in{
        String finalized;

        public in(String finalized){
            this.finalized = finalized;
        }
    }
}
