package com.shubilet.api_gateway.common.constants;

public class ServiceURLs {
    // Expedition Service URLs
    public static final String EXPEDITION_SERVICE_SEARCH_EXPEDITION_URL = "http://expedition-service/api/view/customer/availableExpeditions";
    public static final String EXPEDITION_SERVICE_SEARCH_SEAT_URL = "http://expedition-service/api/view/customer/availableSeats";
    public static final String EXPEDITION_SERVICE_GET_CUSTOMER_TICKETS_SEAT_URL = "http://expedition-service/api/view/customer/allTickets";
    public static final String EXPEDITION_SERVICE_BUY_TICKET = "http://expedition-service/api/reservation/buy_ticket";
    public static final String EXPEDITION_SERVICE_CREATE_EXPEDITION_URL = "http://expedition-service/api/expeditions/create";
    public static final String EXPEDITION_SERVICE_GET_COMPANY_EXPEDITIONS_ALL_URL = "http://expedition-service/api/view/company/allExpeditions";
    public static final String EXPEDITION_SERVICE_GET_COMPANY_EXPEDITIONS_FUTURE_URL = "http://expedition-service/api/view/company/activeExpeditions";
    public static final String EXPEDITION_SERVICE_GET_COMPANY_EXPEDITION_DETAIL_URL = "http://expedition-service/api/view/company/expeditionDetails";
    public static final String EXPEDITION_SERVICE_GET_CUSTOMER_CARDS_URL = "http://expedition-service/api/reservation/view_cards";


    // Member Service URLs
    public static String MEMBER_SERVICE_CREDENTIALS_CHECK_URL = "http://member-service/api/auth/checkCredentials";

    public static String MEMBER_SERVICE_CUSTOMER_REGISTRATION_URL = "http://member-service/api/register/customer";
    public static String MEMBER_SERVICE_COMPANY_REGISTRATION_URL = "http://member-service/api/register/company";
    public static String MEMBER_SERVICE_ADMIN_REGISTRATION_URL = "http://member-service/api/register/admin";

    public static String MEMBER_SERVICE_CUSTOMER_PROFILE_EDIT_NAME_URL = "http://member-service/api/profile/customer/edit/name";
    public static String MEMBER_SERVICE_CUSTOMER_PROFILE_EDIT_SURNAME_URL = "http://member-service/api/profile/customer/edit/surname";
    public static String MEMBER_SERVICE_CUSTOMER_PROFILE_EDIT_GENDER_URL = "http://member-service/api/profile/customer/edit/gender";
    public static String MEMBER_SERVICE_CUSTOMER_PROFILE_EDIT_EMAIL_URL = "http://member-service/api/profile/customer/edit/email";
    public static String MEMBER_SERVICE_CUSTOMER_PROFILE_EDIT_PASSWORD_URL = "http://member-service/api/profile/customer/edit/password";
    public static String MEMBER_SERVICE_FAVORITE_COMPANY_ADD_URL = "http://member-service/api/profile/customer/favoriteCompany/add";
    public static String MEMBER_SERVICE_FAVORITE_COMPANY_DELETE_URL = "http://member-service/api/profile/customer/favoriteCompany/delete";
    public static String MEMBER_SERVICE_CARD_ADD_URL = "http://member-service/api/profile/customer/card/add";
    public static String MEMBER_SERVICE_CARD_DELETE_URL = "http://member-service/api/profile/customer/card/delete";


    public static String MEMBER_SERVICE_GET_COMPANY_NAMES_URL = "http://member-service/api/get/company/name";
    public static final String MEMBER_SERVICE_GET_CUSTOMER_NAMES_URL = "http://member-service/api/get/customer/name";

    public static final String MEMBER_SERVICE_GET_CUSTOMER_PROFILE_URL = "http://member-service/api/profile/customer/get";
    public static final String MEMBER_SERVICE_GET_COMPANY_PROFILE_URL = "http://member-service/api/profile/company/get";
    public static final String MEMBER_SERVICE_GET_ADMIN_PROFILE_URL = "http://member-service/api/profile/admin/get";

    public static final String MEMBER_SERVICE_GET_UNVERIFIED_ADMINS_URL = "http://member-service/api/verification/get/unverified/admins";
    public static final String MEMBER_SERVICE_GET_UNVERIFIED_COMPANIES_URL = "http://member-service/api/verification/get/unverified/companies";
    public static String MEMBER_SERVICE_VERIFY_COMPANY_URL = "http://member-service/api/verification/verify/company";
    public static String MEMBER_SERVICE_VERIFY_ADMIN_URL = "http://member-service/api/verification/verify/admin";


    // Security Service URLs
    public static String SECURITY_SERVICE_CREATE_SESSION_URL = "http://security-service/api/auth/createSession";
    public static String SECURITY_SERVICE_DELETE_SESSION_URL = "http://security-service/api/auth/logout";
    public static String SECURITY_SERVICE_CHECK_SESSION_URL = "http://security-service/api/auth/check";

    public static String SECURITY_SERVICE_CHECK_CUSTOMER_SESSION_URL = "http://security-service/api/auth/checkCustomer";
    public static String SECURITY_SERVICE_CHECK_COMPANY_SESSION_URL = "http://security-service/api/auth/checkCompany";
    public static String SECURITY_SERVICE_CHECK_ADMIN_SESSION_URL = "http://security-service/api/auth/checkAdmin";

}
