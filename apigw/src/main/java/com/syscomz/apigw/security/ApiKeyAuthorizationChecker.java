package com.syscomz.apigw.security;

public interface ApiKeyAuthorizationChecker {

    boolean isAuthorized(String key, String application);

}
