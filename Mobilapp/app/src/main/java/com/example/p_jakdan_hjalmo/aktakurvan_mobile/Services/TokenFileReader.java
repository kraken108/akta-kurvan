package com.example.p_jakdan_hjalmo.aktakurvan_mobile.Services;

import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Jakob on 2017-12-20.
 */

public class TokenFileReader {


    public TokenFileReader(){
        //empty constructor for no reason
    }


    private static String FILENAME = "firebasetoken";

    public String getTheToken(AppCompatActivity activity) throws Exception {
        BufferedReader reader = null;
        InputStream is = null;
        try {
            is = activity.openFileInput(FILENAME);
            reader = new BufferedReader(new InputStreamReader(is));

            String line = reader.readLine();

            if(line == null){
                return "";
            }else{
                return line;
            }


        } catch (IOException e) {
            throw new Exception(e.toString());
        } catch (Exception e) {
            throw new Exception(e.toString());
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
            }
            if(is != null){
                is.close();
            }
        }
    }
}
