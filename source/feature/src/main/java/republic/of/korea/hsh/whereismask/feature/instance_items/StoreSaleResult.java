package republic.of.korea.hsh.whereismask.feature.instance_items;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StoreSaleResult extends BaseInstanceItem {
    long count;
    ArrayList<StoreSale> stores;
    @Override
    protected void _parse(JSONObject json) throws NoSuchFieldException {
        try{
            count = json.getLong("count");
            JSONArray stores_obj = json.getJSONArray("stores");
            try{
                stores.clear();
            }catch(Exception e2){
                stores = new ArrayList<StoreSale>();
            }
            for(int i=0;i<stores_obj.length();i++){
                stores.add(new StoreSale(stores_obj.getJSONObject(i)));
            }
        }catch(Exception e){
            throw new NoSuchFieldException();
        }
    }
    public StoreSaleResult(JSONObject obj) throws NoSuchFieldException{
        super(obj);
    }
    public StoreSaleResult(long count,ArrayList<StoreSale> list){
        super();
        this.count = count;
        try{
            stores.clear();
        }catch(Exception e){
            stores = new ArrayList<StoreSale>();
        }
        stores.addAll(list);
    }
    public final ArrayList<StoreSale> getStores(){
        return stores;
    }
    public final long getCount(boolean dynamic){
        if(dynamic){
            if(stores!=null)return (long)stores.size();
            else return count;
        }
        else return count;
    }
    @Override
    protected void baseAfterInit() {

    }

    @Override
    protected void baseBeforeInit() {
        stores = new ArrayList<StoreSale>();
        count = 0;
    }
}
