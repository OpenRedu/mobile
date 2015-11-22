package br.com.developer.redu.http;

/**
 * Created with IntelliJ IDEA.
 * User: igor
 * Date: 8/30/12
 * Time: 8:46 AM
 * To change this template use File | Settings | File Templates.
 */
import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;

public class ReduOauth2 extends DefaultApi20 {

    private static final String AUTHORIZE_URL = "http://www.redu.com.br/oauth/authorize?client_id=%s";

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "http://redu.com.br/oauth/token?grant_type=authorization_code";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        //Preconditions.checkValidUrl(config.getCallback(), "MUST PROVIDE A VALID URL, REDU DOESNT SUPPORT OOB");
        return String.format(AUTHORIZE_URL, config.getApiKey(),
                OAuthEncoder.encode(config.getCallback()));
    }

    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }


}