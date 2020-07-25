package republic.of.korea.hsh.whereismask.feature.instance_items;

import org.json.JSONObject;

public class Sale extends BaseInstanceItem {
    private long code;
    private String stock_at,created_at;
    private RemainStat remain_stat;
    public enum RemainStat{
        Planty,Some,Few,Empty;
    };
    public long getCode(){
        return code;
    }
    public String getStockAt(){
        return stock_at;
    }
    public String getCreatedAt(){
        return created_at;
    }
    public RemainStat getRemainStat(){
        return remain_stat;
    }
    @Override
    protected void _parse(JSONObject json) throws NoSuchFieldException {
        try{
            code = json.getLong("code");
            stock_at = json.getString("stock_at");
            created_at = json.getString("created_at");
            remain_stat = parseRemainStat(json.getString("remain_stat"));
        }catch(Exception e){
            throw new NoSuchFieldException();
        }
    }
    private RemainStat parseRemainStat(String str){
        if(str.toLowerCase().equals("plenty")){
            return RemainStat.Planty;
        }
        else if(str.toLowerCase().equals("some")){
            return RemainStat.Some;
        }
        else if(str.toLowerCase().equals("few")){
            return RemainStat.Few;
        }
        else{
            return RemainStat.Empty;
        }
    }
    @Override
    protected void baseAfterInit() {

    }

    @Override
    protected void baseBeforeInit() {

    }
}
