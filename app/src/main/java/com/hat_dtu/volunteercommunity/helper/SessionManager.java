package com.hat_dtu.volunteercommunity.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hat_dtu.volunteercommunity.app.AppConfig;

/**
 * Created by paino on 2/22/2017.
 */

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Login";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    //private static String Key_API = "";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        //editor.putString(Key_API, AppConfig.API_KEY);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    //public String getKey(){
      //  return pref.getString(Key_API, AppConfig.API_KEY);
    //}
}
