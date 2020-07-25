package republic.of.korea.hsh.whereismask.feature.instance_items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class StoreResult extends BaseInstanceItem {
    @Override
            public String toString(){
        return String.format("{{'totpg'=%d,'totCnt'=%d,'pg'=%d,'cnt':%d,'storeInfos':%s}}",totalPages,totalCount,page,count,storeInfos.toString());
    }
    long totalPages,totalCount,page,count;
    ArrayList<Store> storeInfos;
    public ArrayList<Store> getStoreInfos(){
        return storeInfos;
    }
    public long getTotalPages(){
        return totalPages;
    }
    public long getTotalCount(){
        return totalCount;
    }
    public long getPage(){
        return page;
    }
    public long getCount(){
        return count;
    }
    @Override
    protected void _parse(JSONObject json) throws NoSuchFieldException {
        try{
            totalPages = json.getLong("totalPages");
            totalCount = json.getLong("totalCount");
            page = json.getLong("page");
            count = json.getLong("count");
            JSONArray store_infos = json.getJSONArray("storeInfos");
            try{
                storeInfos.clear();
            }catch(Exception e2){

            }
            for(int i=0;i<store_infos.length();i++){
                JSONObject tmp_obj = store_infos.getJSONObject(i);
                storeInfos.add(new Store(tmp_obj));
            }
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            android.util.Log.e("_parse error",sw.toString());
            throw new NoSuchFieldException();
        }
    }
    public StoreResult(long totalPages,long totalCount,long page,long count,ArrayList<Store> storeInfos){
        super();
        this.totalPages = totalPages;
        this.totalCount = totalPages;
        this.page = page;
        this.count = count;
        try {
            this.storeInfos.clear();
        }catch(Exception e){

        }
        this.storeInfos.addAll(storeInfos);
    }
    public StoreResult(JSONObject obj)throws NoSuchFieldException{
        super(obj);
    }
    @Override
    protected void baseAfterInit() {

    }

    @Override
    protected void baseBeforeInit() {
        storeInfos = new ArrayList<Store>();
        totalPages =totalCount=0;
        page=count = -1;
    }
}
