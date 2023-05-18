 package com.upicon.app.AppController;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

 public class BaseURL {

     public static final String TOKEN= "08ee418395cb6e2c8df58eb949feaadc";
     public static String BASE_PATH = "https://www.upicondashboard.in/";
     private static final String BASE_FOLDER= BASE_PATH + "TrainingAPI/";
     public static final String SLIDER_IMAGE= BASE_FOLDER + "images/slider/";
     public static final String APP_UPDATE= BASE_FOLDER+ "AppUpdate.php";
     public static final String USER_LOGIN= BASE_FOLDER+ "UserLogin.php";
     public static final String SURVEYOR_REGISTRATION= BASE_FOLDER+ "SurveyorRegistration.php";
     public static final String USER_SESSION= BASE_FOLDER+ "UserSession.php";
     public static final String USER_LOGOUT= BASE_FOLDER+ "UserLogout.php";
     public static final String USER_LIST= BASE_FOLDER+ "UserList.php";
     public static final String USER_PROFILE= BASE_FOLDER+ "UserProfile.php";
     public static final String CHANGE_PASSWORD= BASE_FOLDER+ "ChangePassword.php";
     public static final String UPDATE_USER_STATUS= BASE_FOLDER+ "UpdateUser.php";
     public static final String APPLICATION_LIST= BASE_FOLDER+ "ApplicationList.php";
     public static final String ADD_NEW_APPLICATION= BASE_FOLDER+ "AddNewApplication.php";
     public static final String GET_DEPT_BY_CODE= BASE_FOLDER+ "GetDepartmentByCode.php";
     public static final String GET_DEPT_BY_ID= BASE_FOLDER+ "GetDepartmentByID.php";
     public static final String ADD_NEW_AUDIT= BASE_FOLDER+ "AddNewAudit.php";
     public static final String AUDIT_LIST= BASE_FOLDER+ "AuditList.php";
     public static final String DEPARTMENT_LIST= BASE_FOLDER+ "DepartmentList.php";
     public static final String QUESTION_LIST= BASE_FOLDER+ "QuestionList.php";
     public static final String ADD_QUESTION= BASE_FOLDER+ "AddQuestion.php";




    public static boolean isOnline(Context applicationContext) {

        ConnectivityManager cm = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }


}
