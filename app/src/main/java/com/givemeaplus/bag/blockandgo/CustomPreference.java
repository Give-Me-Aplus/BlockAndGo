package com.givemeaplus.bag.blockandgo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Hye Jeong on 2015-12-07.
 */
public class CustomPreference {
    static CustomPreference mPreferenceTest;

    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    private CustomPreference(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();

        if(preferences.contains("BackgroundMusicState")) {
            editor.putBoolean("BackgroundMusicState", true);
            editor.commit();
        }

        if(preferences.contains("EffectSoundState")) {
            editor.putBoolean("EffectSoundState", true);
            editor.commit();
        }

        if(preferences.contains("UserName")) {
            editor.putString("UserName", " ");
            editor.commit();
        }

    }

    public static CustomPreference getInstance(Context context){
        if(preferences==null) mPreferenceTest = new CustomPreference(context);
        return mPreferenceTest;
    }

    public void setBackgroundMusicState(boolean state){

        editor.putBoolean("BackgroundMusicState", state);
        editor.commit();
    }

    public void setEffectSoundState(boolean state){

        editor.putBoolean("EffectSoundState", state);
        editor.commit();
    }

    public void setUserName(String name){
        editor.putString("UserName", "name");
        editor.commit();
    }

    public boolean getBackgroundMusicState(){
        return preferences.getBoolean("BackgroundMusicState", true);
    }

    public boolean getEffectSoundState(){
        return preferences.getBoolean("EffectSoundState", true);
    }

    public String setUserName(){
        return preferences.getString("UserName", "name");
    }
}
