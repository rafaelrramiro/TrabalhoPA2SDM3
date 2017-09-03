package br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.nio.charset.StandardCharsets;

/**
 * Created by Note on 03/09/2017.
 */

public class Utils {

    public static final String appNameSharedPreferences = "br.edu.ifspsaocarlos.sdm.trabalhopa2sdm3.ladainha";

    public static final String appNameCode = "CHATLADAINHA";

    public static final String SP_CURRENT_USER = "CURRENT_USER";

    public static String getAppIdBase64(String userName){
        return Base64.encodeToString(
                (appNameCode+userName.substring(0, userName.length()>=5?5:userName.length()-1)).getBytes(StandardCharsets.UTF_8),
                Base64.NO_WRAP);
    }

    public static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(appNameSharedPreferences,Context.MODE_PRIVATE);
    }

    public static Contato getContatoLogado(Context context) {
        return new Gson().fromJson(getPreferences(context).getString(Utils.SP_CURRENT_USER,""), Contato.class);
    }

}
