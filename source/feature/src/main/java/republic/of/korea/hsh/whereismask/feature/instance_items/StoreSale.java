package republic.of.korea.hsh.whereismask.feature.instance_items;

import org.json.JSONObject;

public class StoreSale extends BaseInstanceItem {
    private static final float FLOAT_NULL = -99999;
    public boolean isGPSnull() {
        return latitude==FLOAT_NULL||longitude==FLOAT_NULL;
    }
    public StoreSale(JSONObject obj) throws NoSuchFieldException{
        super(obj);
    }
    public StoreSale(long code,String name,String addr,Object type,float latitude,float longitude,String stock_at,String remain_stat,String created_at){
        super();
        this.code = code;
        this.name = name;
        this.addr = addr;
        if(type!=null){
            if(type instanceof String){
                mType = parseType((String)type);
            }
            else if(type instanceof Type){
                mType = (Type)type;
            }
            else{
                mType = Type.unknown;
            }
        }
        this.latitude = latitude;
        this.longitude = longitude;
        this.stock_at = stock_at;
        this.remain_stat = RemainStat.parseRemainStat(remain_stat);
        this.created_at = created_at;
    }
    public long getCode(){
        return code;
    }
    public String getName(){
        return name;
    }
    public String getAddress(){
        return addr;
    }
    public String getStockAt(){
        return stock_at;
    }
    public RemainStat getRemainStat(){
        return remain_stat;
    }
    public String getCreatedAt(){
        return created_at;
    }
    public Type getType(){
        return mType;
    }
    public float getLatitude(){
        return latitude;

    }
    public float getLongitude(){
        return longitude;
    }
    private long code;
    private String name,addr,stock_at,created_at;
    private RemainStat remain_stat;
    private Type mType;
    float latitude,longitude;
    private Type parseType(String dt){
        try{
            final int num = Integer.parseInt(dt);
            switch(num){
                case 1:{
                    return Type.pharmacy;
                }
                case 2:{
                    return Type.postoffice;
                }
                case 3:{
                    return Type.nonghyup;
                }
                default:return Type.unknown;
            }
        }catch(Exception e){
            return Type.unknown;
        }
    }
    public enum RemainStat{
        Plenty("plenty"),Some("some"),Few("few"),Empty("empty"),Break("break"),Unknown("unknown");
        private String stat_string="unknown";
        RemainStat(String str){
            stat_string = str;
        }
        @Override
        public String toString(){
            return stat_string;
        }
        public static RemainStat parseRemainStat(String stat){
            try{
                stat= stat.toLowerCase();
                if(stat.equals("plenty")){
                    return RemainStat.Plenty;
                }
                else if(stat.equals("some")){
                    return RemainStat.Some;
                }
                else if(stat.equals("few")){
                    return RemainStat.Few;
                }
                else if(stat.equals("empty")){
                    return RemainStat.Empty;
                }
                else if(stat.equals("break")){
                    return RemainStat.Break;
                }
                else{
                    return RemainStat.Unknown;
                }
            }
            catch(Exception e){
                return RemainStat.Unknown;
            }
        }

    }
    public enum Type{
        pharmacy(1),postoffice(2),nonghyup(3),unknown(-1);
        private int type_code;
        Type(int d){
            type_code=d;
        }
        public final int getTypeCode(){
            return type_code;
        }
        @Override
        public String toString(){
            switch(type_code){
                case 1:
                    return "약국";
                case 2:
                    return "우체국";
                case 3:
                    return "농협";
                default:return String.format("%02d",type_code);
            }
        }
    }
    @Override
    protected void _parse(JSONObject json) throws NoSuchFieldException {
        try{
            this.code = json.getInt("code");
            this.name = json.getString("name");
            this.addr = json.getString("addr");
            mType = parseType( json.getString("type"));

                if(json.isNull("lat"))
                    latitude = FLOAT_NULL;
                else
                    latitude = (float)json.getDouble("lat");
                if(json.isNull("lat"))
                    longitude = FLOAT_NULL;
                else
                    longitude = (float)json.getDouble("lng");


            stock_at = json.getString("stock_at");
            remain_stat = RemainStat.parseRemainStat( json.getString("remain_stat"));
            created_at = json.getString("created_at");

        }catch(Exception e){
            throw new NoSuchFieldException();
        }
    }

    @Override
    protected void baseAfterInit() {

    }

    @Override
    protected void baseBeforeInit() {

    }
}
