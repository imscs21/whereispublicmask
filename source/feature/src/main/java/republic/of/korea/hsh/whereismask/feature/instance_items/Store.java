package republic.of.korea.hsh.whereismask.feature.instance_items;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Store extends BaseInstanceItem {
    String name,addr;
    long code;
    private Type mType;
    float latitude,longitude;
    private static final float FLOAT_NULL = -99999;
    public boolean isGPSnull() {
        return latitude==FLOAT_NULL||longitude==FLOAT_NULL;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getAddr() {
        return addr;
    }

    public long getCode() {
        return code;
    }

    public Type getType() {
        return mType;
    }

    @Override
    protected void _parse(JSONObject json) throws NoSuchFieldException{
        try{
            code = json.getLong("code");
            name = json.getString("name");
            addr = json.getString("addr");
            mType = parseType(json.getString("type"));
            if(json.isNull("lat"))
                latitude = FLOAT_NULL;
                else
            latitude = (float)json.getDouble("lat");
                if(json.isNull("lat"))
                    longitude = FLOAT_NULL;
                    else
            longitude = (float)json.getDouble("lng");
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            android.util.Log.e("Store_parse error",sw.toString());
            throw new NoSuchFieldException();
        }
    }
    public Store(JSONObject obj) throws NoSuchFieldException{
        super(obj);
    }
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
            return Store.Type.unknown;
        }
    }
    @Override
    protected void baseAfterInit() {

    }

    @Override
    protected void baseBeforeInit() {

    }
    @Override
    public String toString(){
        return String.format("{addr:'%s',code:%d,lat:%.2f,lng:%.2f,name:'%s',type:%d}",addr,code,latitude,longitude,name,mType.getTypeCode());
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


}
