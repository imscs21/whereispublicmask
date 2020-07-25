package republic.of.korea.hsh.whereismask.feature.instance_items;

import org.json.JSONObject;

public abstract class BaseInstanceItem {
    protected abstract void _parse(JSONObject json)throws NoSuchFieldException;
    protected abstract void baseAfterInit();
    protected abstract void baseBeforeInit();
    public BaseInstanceItem parseIndirectly(JSONObject jsonObject,boolean useInitFunc) throws  NoSuchFieldException{
        if(useInitFunc)
        baseBeforeInit();
        _parse(jsonObject);
        if(useInitFunc)
            baseAfterInit();
        return this;
    }
    protected BaseInstanceItem(){
        baseBeforeInit();
        baseAfterInit();
    }
    public BaseInstanceItem(JSONObject json) throws NoSuchFieldException{
        baseBeforeInit();
        _parse(json);
        baseAfterInit();
    }

}
