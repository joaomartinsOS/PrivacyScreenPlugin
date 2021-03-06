/**
 * PrivacyScreenPlugin.java Cordova Plugin Implementation
 * Created by Tommy-Carlos Williams on 18/07/14.
 * Copyright (c) 2014 Tommy-Carlos Williams. All rights reserved.
 * MIT Licensed
 */
package org.devgeeks.privacyscreen;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import org.apache.cordova.CallbackContext ;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONException;


import org.json.JSONObject;
import org.json.JSONArray;
/**
 * This class sets the FLAG_SECURE flag on the window to make the app
 * private when shown in the task switcher
 */
public class PrivacyScreenPlugin extends CordovaPlugin {
    private static final String CHANGE_PRIVACY = "changePrivacy";
    private static final String GET_VALUE = "getPreferencesValue";
    public static final String KEY_PRIVACY_SCREEN_ENABLED = "org.devgeeks.privacyscreen/PrivacyScreenEnabled";
    private SharedPreferences preferences;
    public CallbackContext callbackContext;
    private Activity activity;
    /*private WindowManager.LayoutParams params;
    private WindowManager wm;
    private View currView;*/

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        activity = this.cordova.getActivity();
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean privacyScreenEnabled = isPrivacyScreenEnabled(true);
/*
        params = activity.getWindow().getAttributes();
        wm = (WindowManager)activity.getSystemService(Context.WINDOW_SERVICE);
        currView = webView.getRootView();
*/
        if (privacyScreenEnabled) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        }
    }


    /**
     * Executes the request and returns PluginResult.
     *
     * @param args            JSONArry of arguments for the plugin.
     * @param callbackContext The callback id used when calling back into JavaScript.
     * @return A PluginResult object with a status and message.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        JSONObject obj = args.optJSONObject(0);
        boolean IsToShowFlag;
        Activity thisActivity = cordova.getActivity();
        if (obj != null) {
            IsToShowFlag = obj.optBoolean("IsToShowFlag");
        } else {
            callbackContext.error("User did not specify IsToShowFlag");
            return true;
        }

        if (action.equals(CHANGE_PRIVACY)) {
            try {
                if(!IsToShowFlag)
                {
                    thisActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                    thisActivity.recreate();
                } else {
                    thisActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                    thisActivity.recreate();
                }
            } catch (Exception e) {
                callbackContext.error("Error");
                PluginResult r = new PluginResult(PluginResult.Status.ERROR);
                callbackContext.sendPluginResult(r);
                return true;
            }
        } else if (action.equals(GET_VALUE)){
            try {
            JSONObject prefs = new JSONObject();
            prefs.put("preferencevalue", getPreferencesValue());
            callbackContext.success(prefs);
            } catch (Exception e) {
                callbackContext.error("Error getting the value");
                PluginResult r = new PluginResult(PluginResult.Status.ERROR);
                callbackContext.sendPluginResult(r);
                return true;
            }
        } else {
            return false;
        }

        PluginResult r = new PluginResult(PluginResult.Status.OK);
        r.setKeepCallback(true);
        callbackContext.sendPluginResult(r);

        return true;
    }

    /**
     * Gets the value of the Privacy Screen Enabled entry stored in {@link SharedPreferences}.
     * Will not attempt to correct an erroneous entry.
     *
     * @param defValue The default value of the preference if not readable or non-existent.
     * @return Whether the privacy screen should be enabled during the next application launch.
     * @see #KEY_PRIVACY_SCREEN_ENABLED
     * @see #isPrivacyScreenEnabled(boolean, boolean)
     */
    private boolean isPrivacyScreenEnabled(boolean defValue) {
        return isPrivacyScreenEnabled(defValue, false);
    }

    /**
     * Gets the value of the Privacy Screen Enabled entry stored in {@link SharedPreferences}.
     *
     * @param defValue      The default value of the preference if not readable or non-existent.
     * @param shouldCorrect If true, will attempt to convert String value to Boolean or replace with the default value.
     * @return Whether the privacy screen should be enabled during the next application launch.
     * @see #KEY_PRIVACY_SCREEN_ENABLED
     */
    private boolean isPrivacyScreenEnabled(boolean defValue, boolean shouldCorrect) {
        if (!preferences.contains(KEY_PRIVACY_SCREEN_ENABLED)) {
            setPrivacyScreenEnabled(defValue);
            return defValue;
        }
        try {
            return preferences.getBoolean(KEY_PRIVACY_SCREEN_ENABLED, defValue);
        } catch (ClassCastException e) {
            Log.w("PrivacyScreen", "SharedPreference '" + KEY_PRIVACY_SCREEN_ENABLED + "' was not a Boolean value.", e);

            if (shouldCorrect) {
                if (convertStringEntryToBoolean()) {
                    return preferences.getBoolean(KEY_PRIVACY_SCREEN_ENABLED, defValue);
                }
                setPrivacyScreenEnabled(defValue);
            }
        }
        return defValue;
    }

    /**
     * Converts the entry from a {@link String} to {@link Boolean} if possible.
     *
     * @return true if the entry was a {@code String} value that could be resolved to a {@code Boolean} value, false otherwise.
     * @see #isPrivacyScreenEnabled(boolean, boolean)
     * @see #KEY_PRIVACY_SCREEN_ENABLED
     */
    private boolean convertStringEntryToBoolean() {
        try {
            String val = preferences.getString(KEY_PRIVACY_SCREEN_ENABLED, null);
            if (val == null) {
                return false;
            }
            if (val.equalsIgnoreCase("true")) {
                setPrivacyScreenEnabled(true);
                return true;
            } else if (val.equalsIgnoreCase("false")) {
                setPrivacyScreenEnabled(false);
                return true;
            }
        } catch (ClassCastException e) {
            Log.w("PrivacyScreen", "SharedPreference '" + KEY_PRIVACY_SCREEN_ENABLED + "' was not a String value.", e);
        }
        return false;
    }

    /**
     * Sets the value of the Privacy Screen Enabled entry in {@link SharedPreferences}.
     *
     * @param value the value to set.
     * @return true if successful, false otherwise.
     * @see #KEY_PRIVACY_SCREEN_ENABLED
     */
    private boolean setPrivacyScreenEnabled(boolean value) {
        preferences.edit().putBoolean(KEY_PRIVACY_SCREEN_ENABLED, value).apply();
        return true;
    }

    private boolean actualyChangeFlag() {
        Activity thisActivity = cordova.getActivity();

        if (isPrivacyScreenEnabled(true, true)) {
            setPrivacyScreenEnabled(true);
            thisActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
            thisActivity.recreate();
            return true;
        } else {
            setPrivacyScreenEnabled(false);
            thisActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
            thisActivity.recreate();
            return true;
        }

    }

    private String getPreferencesValue(){
        String outputText = "The preferences Value is: ";
        outputText += isPrivacyScreenEnabled(true, true);
        return outputText;
    }

    @Override
    public Object onMessage(String id, Object data) {
        if (id == "isPrivacyScreenEnabled") {
            boolean shouldCorrect = (data instanceof Boolean) && (data != null) && ((Boolean) data);
            return isPrivacyScreenEnabled(true, shouldCorrect);
        } else if (id == "setPrivacyScreenEnabled") {
            if (data instanceof Boolean && data != null) {
                return setPrivacyScreenEnabled((Boolean) data);
            }
            return false;
        }
        return null;
    }
}