package republic.of.korea.hsh.whereismask.feature.instance_items;

import org.json.JSONObject;

import java.util.ArrayList;

public final class SaleResult extends BaseInstanceItem {
    private long totalPages,totalCount,page,count;
    private ArrayList<Sale> sales;
    public final long getTotalPages(){
        return totalPages;
    }
    public final long getTotalCount(){
        return totalCount;

    }
    public final long getPage(){
        return page;
    }
    public final long getCount(){
        return count;
    }
    public final ArrayList<Sale> getSales(boolean deepcopy){
        if(deepcopy){
            ArrayList<Sale> rst = new ArrayList<Sale>();
            rst.addAll(this.sales);
            return rst;
        }
        else return sales;
    }
    @Override
    protected void _parse(JSONObject json) throws NoSuchFieldException {
        try{
            totalPages = json.getLong("totalPages");
            totalCount = json.getLong("totalCount");
            page = json.getLong("page");
            count = json.getLong("sales");

        }catch(Exception e){
            throw new NoSuchFieldException();
        }
    }
    public SaleResult(long totalPages,long totalCount,long page,long count,ArrayList<Sale> sales){
        super();
        this.totalPages = totalPages;
        this.totalCount = totalCount;
        this.page = page;
        this.count =count;
        try{
            this.sales.clear();
        }
        catch(Exception e){
            this.sales = new ArrayList<Sale>();
        }
        this.sales.addAll(sales);
    }
    public SaleResult(JSONObject obj) throws NoSuchFieldException{
        super(obj);
    }
    @Override
    protected void baseAfterInit() {

    }

    @Override
    protected void baseBeforeInit() {
        sales = new ArrayList<Sale>();
        totalPages = totalCount =0;
        page=count=-1;
    }
}
