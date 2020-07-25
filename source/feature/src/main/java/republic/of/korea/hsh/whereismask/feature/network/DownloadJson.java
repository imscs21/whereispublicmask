package republic.of.korea.hsh.whereismask.feature.network;

import android.os.AsyncTask;
import android.os.Build;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import republic.of.korea.hsh.whereismask.feature.instance_items.BaseInstanceItem;
import republic.of.korea.hsh.whereismask.feature.instance_items.StoreResult;

public class DownloadJson<T extends BaseInstanceItem> extends AsyncTask<Object,Integer,T> {
    public static String prefix_url = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1";
    public static String sufix_url_store = "/stores/json";
    public static String sufix_url_sales = "/sales/json";
    public static String sufix_url_stores_by_gps = "/storesByGeo/json";
    public static String sufix_url_stores_by_addr = "/storesByAddr/json";
    public static String URL_STORE = prefix_url+sufix_url_store;
    public static String URL_SALES = prefix_url+sufix_url_sales;
    public static String URL_STORES_BY_GPS = prefix_url+sufix_url_stores_by_gps;
    public static String URL_STORES_BY_ADDR = prefix_url+sufix_url_stores_by_addr;
    public static class URLgetQueryBuilder{
        private Map<String,String> mMap;
        public URLgetQueryBuilder(){
            mMap = new HashMap<String,String>();

        }
        public URLgetQueryBuilder addParameter(String key,String value){
            mMap.put(key, value);
            return this;
        }
        public URLgetQueryBuilder replaceParameter(String key,String value){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
                mMap.replace(key, value);
            }
            else{
                mMap.put(key, value);
            }
            return this;
        }
        public URLgetQueryBuilder removeParameter(String key){
            if(mMap.containsKey(key)){
                mMap.remove(key);
            }
            return this;
        }
        public URLgetQueryBuilder addOrReplaceParameter(String key,String value){
            if(mMap.containsKey(key)){
                replaceParameter(key, value);
            }
            else{
                addParameter(key, value);
            }
            return this;
        }
        public String build(final boolean clear){
            String rst="";
            boolean first = true;
            for(String k:mMap.keySet()){
                if(first){
                    first = false;
                }
                else{
                    rst+="&";
                }
                 String val = mMap.get(k);
                try {
                    val = URLEncoder.encode(val, "UTF-8");
                }catch(Exception e){

                }
                rst+=String.format("%s=%s",k,val);
            }
            if(!rst.trim().equals(""))
            rst = "?"+rst;
            if(clear){
                mMap.clear();
            }

            return rst;
        }
    }
    @Override
    protected void onPostExecute(T jsonObject) {
        super.onPostExecute(jsonObject);
        try {
            this.cancel(true);
        }catch(Exception e){

        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected T doInBackground(Object... strings) {
        final String full_url_string = (String)strings[1];
        final Class<T> cls = (Class<T>)strings[0];
        final String url_param = (String)strings[2];
        android.util.Log.e("url param:",String.format("'%s'",url_param));
        final String combined_url = full_url_string+url_param;
        android.util.Log.e("full url:",String.format("'%s'",combined_url));
        try {
            URL url = new URL(combined_url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            /*conn.setRequestProperty("Accept","application/json");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Cache-Control","no-cache");

            conn.setDoInput(true);
            conn.setDoOutput(true);*/
            //conn.setUseCaches();
            conn.setRequestMethod("GET");
            int resp_code = conn.getResponseCode();
            switch (resp_code){
                case HttpURLConnection.HTTP_OK:
                case HttpURLConnection.HTTP_ACCEPTED:
                {
                    T rst = null;
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while((line=br.readLine())!=null){
                        sb.append(line+"\n");
                    }
                    JSONObject objs = new JSONObject(sb.toString());
                    //android.util.Log.e("construtors"+cls.getConstructors().length,cls.getConstructors().toString());
                    //rst = (T)(new StoreResult(objs));
                    rst = (T)cls.getConstructors()[1].newInstance(objs);
                    //cls.getConstructors()[0].
                    //cls.getConstructors()[0].newInstance()
                    //rst = (T)rst.parseIndirectly(objs,false);

                    //rst = new T(sb.toString());
                    sb.setLength(0);
                    sb =null;
                    /*if(full_url_string.endsWith(sufix_url_store)){

                    }*/
                    return rst;
                }
                default:
                    android.util.Log.e("http resp code:",resp_code+"");
                    try{
                        conn.disconnect();
                    }catch(Exception e2){

                    }
                    return null;
            }
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            android.util.Log.e(getClass().toString(),sw.toString());
            return null;
        }

    }
}
