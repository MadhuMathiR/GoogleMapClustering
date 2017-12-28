package com.googlemapclustering.webservice;


public interface WebServiceListener {
    public void onError(String message, String response);

    public void onSuccess(final String message, String response, Object object);

}
