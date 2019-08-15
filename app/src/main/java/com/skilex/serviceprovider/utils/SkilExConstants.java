package com.skilex.serviceprovider.utils;

/**
 * Created by Admin on 23-09-2017.
 */

public class SkilExConstants {

    //    URLS
    //    BASE URL
    private static final String BASE_URL = "https://skilex.in/";

    //BUILD URL
    public static final String BUILD_URL = BASE_URL + "development/apisprovider/";

    //NUMBER VERIFICATION URL FOR LOGIN
    public static final String MOBILE_VERIFICATION = "mobile_check/";

    //Register with basic info
    public static final String REGISTER_SERVICE_PROVIDER = "register/";
    //Service provider registration basic field
    public static String REGISTRATION_NAME = "name";
    public static String REGISTRATION_PHONE_NUMBER = "mobile";
    public static String REGISTRATION_EMAIL_ID = "email";

    //List all categories
    public static final String LIST_ALL_CATEGORIES = "category_list/";

    //    Service Params
    public static String PARAM_MESSAGE = "msg";

    //     Shared preferences file name
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    //    Shared FCM ID
    public static final String KEY_FCM_ID = "fcm_id";

    //    Shared IMEI No
    public static final String KEY_IMEI = "imei_code";

    // Alert Dialog Constants
    public static String ALERT_DIALOG_TITLE = "alertDialogTitle";
    public static String ALERT_DIALOG_MESSAGE = "alertDialogMessage";
    public static String ALERT_DIALOG_TAG = "alertDialogTag";
    public static String ALERT_DIALOG_POS_BUTTON = "alert_dialog_pos_button";
    public static String ALERT_DIALOG_NEG_BUTTON = "alert_dialog_neg_button";

    //    Shared Phone No
    public static final String KEY_MOBILE_NUMBER = "mobile_number";

    //  Shared User Master Id
    public static final String KEY_USER_MASTER_ID = "store_user_master_id";

    //Select category
    public static final String KEY_USER_HAS_PREFERENCES = "hasPreferences";

    //    Shared UserID
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_TYPE = "user_type";
    //    Shared Categories Id
    public static final String KEY_CATEGORIES_ID = "category_ids";

    // Category save
    public static final String USER_CATEGORY_UPDATE = "serv_prov_category_add/";

    // Organization type
    public static final String ORGANIZATION_TYPE_SELECTION = "update_company_status/";
    public static final String KEY_TYPE_SELECTION = "company_type";

    // Individual type organization
    public static final String PROVIDER_INDIVIDUAL_DETAILS = "add_individual_status/";

    public static final String KEY_NO_OF_SERVICE_PERSON = "no_of_service_person";
    public static final String KEY_ALSO_A_SERVICE_PERSON = "also_service_person";

    // Upload unregistered organization document upload
    public static final String UPLOAD_DOCUMENT = "upload_doc/";

    //Login
    public static String LOGIN = "login/";
    // Login Parameters
    public static String PHONE_NUMBER = "phone_no";
    public static String OTP = "otp";
    public static String DEVICE_TOKEN = "device_token";
    public static String MOBILE_TYPE = "mobile_type";
    public static String USER_MASTER_ID = "user_master_id";
    public static String UNIQUE_NUMBER = "unique_number";
    public static String MOBILE_KEY = "mobile_key";
    public static String USER_STATUS = "user_stat";

}
