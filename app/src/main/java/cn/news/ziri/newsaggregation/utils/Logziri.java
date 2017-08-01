package cn.news.ziri.newsaggregation.utils;

import android.util.Log;

/**
 * Created by ward on 2017/7/29.
 */

public class Logziri {
    private static boolean debugable=true;
    private static String Tag="NewsAggregation";
    public static void d(String message){
         if(debugable) Log.d(Tag,message);
    }

    public static void i(String message){
         Log.i("Tag",message);
    }
}
