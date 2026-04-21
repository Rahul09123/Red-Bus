package com.shubilet.expedition_service.common.constants;

public class ServiceURLs {
    
    ///GENERAL URLS
    private static final String PAYMENT_SERVICE_URL = "http://payment-service";
    

    ///PAYMENT SERVICE URLS
    public static final String PAYMENT_SERVICE_CUSTOMER_CARDS = PAYMENT_SERVICE_URL + "/cards/customer";
    public static final String PAYMENT_SERVICE_CHECK_ACTIVATE = PAYMENT_SERVICE_URL + "/cards/check-active";
    public static final String PAYMENT_SERVICE_MAKE_PAYMENT = PAYMENT_SERVICE_URL + "/payment";
    
}
