package republic.of.korea.hsh.whereismask.feature.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class PopulationDatabaseLoader  {
    private static final String DB_DEFAULT_NAME = "population.db";
    private static final String DB_PATH = "file://android_asset/"+DB_DEFAULT_NAME;
    public PopulationDatabaseLoader(){
        init();
    }
    private SQLiteDatabase db;
    private void init(){
        db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
        //if (db != null && db.isOpen())return db;
    }

    public ArrayList<QueryPair<String,Integer>> getPopulation(LOAD_AREA_TYPE area_type, DAY_TYPE day_type, String... args){
        ArrayList<QueryPair<String,Integer>> rst = new ArrayList<QueryPair<String,Integer>>();
        String area_conditions = "";
        if(area_type==LOAD_AREA_TYPE.LOAD_BY_CITY){
            area_conditions = String.format("and city = '%s' and village is NULL",args[0]);
        }
        else if(area_type==LOAD_AREA_TYPE.LOAD_BY_STATE){
            area_conditions = String.format("and state = '%s' and city is NULL and village is NULL",args[0]);
        }
        else if(area_type==LOAD_AREA_TYPE.LOAD_BY_VILLAGE){
            area_conditions = String.format("and village = '%s'",args[0]);
        }
        else if(area_type==LOAD_AREA_TYPE.LOAD_BY_CITY_AND_VILLAGE){
            area_conditions = String.format("and city = '%s' and village = '%s'",args[0],args[1]);

        }
        else if(area_type==LOAD_AREA_TYPE.LOAD_BY_STATE_AND_VILLAGE){
            area_conditions = String.format("and state = '%s' and village = '%s'",args[0],args[1]);
        }
        else if(area_type==LOAD_AREA_TYPE.LOAD_BY_STATE_N_CITY_AND_VILLAGE){
            area_conditions = String.format("and state = '%s' and city = '%s' and village = '%s'",args[0],args[1],args[2]);
        }
        else if(area_type==LOAD_AREA_TYPE.LOAD_ALL){
            area_conditions = "";
        }
        Cursor c = db.rawQuery(String.format("select state,city,village,요일 as day,몰릴_수_있는_최대_인원수 as max_cnt from 시도군별분포2020 where 요일 like '?' %s",area_conditions),new String[]{day_type.getDay()});
        while(c.moveToNext()){
            rst.add(new QueryPair<String, Integer>(c.getString(c.getColumnIndex("day")),c.getInt(c.getColumnIndex("max_cnt"))));
        }
        return rst;
    }
    public class QueryPair<T,K>{
        T t;
        K k;
        public T getFirst(){
            return t;
        }
        public K getSecond(){
            return k;
        }
        public QueryPair(T t,K k){
            this.t = t;
            this.k = k;
        }

    }
    public enum LOAD_AREA_TYPE{
        LOAD_ALL,
        LOAD_BY_CITY,
        LOAD_BY_STATE,
        LOAD_BY_VILLAGE,
        LOAD_BY_STATE_N_CITY_AND_VILLAGE,
        LOAD_BY_CITY_AND_VILLAGE,
        LOAD_BY_STATE_AND_VILLAGE
    }
    public enum DAY_TYPE{
        WeekDay("%"),Monday("월"),Tuesday("화"),Wednesday("수"),Thursday("목"),Friday("금");
        private String raw_day;
        DAY_TYPE(String d){
            raw_day = d;
        }
        public String getDay(){
            return raw_day;
        }
        @Override
        public String toString(){
            return getDay();
        }
    }
    public void onDestroy(){
        if(db!=null){

        }
    }
}
