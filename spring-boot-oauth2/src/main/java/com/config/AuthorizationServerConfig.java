package com.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Value("${security.client-id}")
	private String clientId;

	@Value("${security.client-secret}")
	private String clientSecret;

	@Value("${security.grant-type}")
	private String grantType;

	@Value("${security.scope-read}")
	private String scopeRead;

	@Value("${security.scope-write}")
	private String scopeWrite;
	
	@Value("${security.token-validity}")
	private int tokenValidity;

	@Value("${security.resource-ids}")
	private String resourceIds;
	
	@Autowired
	private TokenStore tokenStore;
	

	@Autowired
	//@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;

 
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(clientId)
                .secret(clientSecret)
                .authorizedGrantTypes(grantType)
                .scopes(scopeRead,scopeWrite)
                .resourceIds(resourceIds)
                .accessTokenValiditySeconds(tokenValidity); 
    } 

    @Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    	
    	TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
		
		endpoints.tokenStore(tokenStore)
		        .accessTokenConverter(accessTokenConverter)
		        .tokenEnhancer(enhancerChain)
		        .authenticationManager(authenticationManager);
    }
}
