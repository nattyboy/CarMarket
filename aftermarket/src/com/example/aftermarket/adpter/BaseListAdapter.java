package com.example.aftermarket.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.aftermarket.R;

/**
 * Created by EASTMOOD on 2015/12/22.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    private List<T> list;
    protected LayoutInflater inflater;
    private Context context;
    protected View rowView;
    private View zeroView;
    private int emptyId;

    public BaseListAdapter(Context context) {
        this( context , R.layout.item_empty );
    }

    public BaseListAdapter(Context context, int emptyId){
        this.context = context;
        this.emptyId = emptyId;
        this.inflater = LayoutInflater.from(context);
    }

    public void setItems(List var1, boolean var2) {

        if (var1 == null){
            this.zeroView = getNullView();
        }else{
            this.zeroView = getEmptyView();
        }

        if(!var2) {
            this.list = new ArrayList();
        }

        if(var1 != null && var1.size() > 0) {
            this.list.addAll(var1);
        }

        this.notifyDataSetChanged();
    }

    public void addItem(T var1) {
        if(this.list == null) {
            this.list = new ArrayList();
        }

        this.list.add(var1);
        this.notifyDataSetChanged();
    }

    public void modify(int var1, T var2) {
        if(this.list != null && this.list.size() - 1 >= var1) {
            this.list.remove(var1);
            this.list.add(var1, var2);
            this.notifyDataSetChanged();
        }
    }

    public boolean isEmpty() {
        return this.list == null || this.list.size() == 0;
    }

    public void remove(int position) {
        this.list.remove(position);
        this.notifyDataSetChanged();
    }

    public List<T> getItems() {
        return this.list;
    }

    protected View inflate(int var1, ViewGroup var2) {
        return this.inflater.inflate(var1, var2);
    }

    public int getCount() {
        return this.list == null?0:(this.list.size() == 0?0:this.list.size());
    }

    public T getItem(int position) {
        return this.list != null && this.list.size() != 0?( T ) this.list.get(position) : null;
    }

    public long getItemId(int var1) {
        return 0L;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(this.list.size() == 0) {
            convertView =  zeroView;
            return convertView;
        } else {
            convertView = null;
            return this.getListView(position, convertView, viewGroup);
        }
    }

    public boolean isEnabled(int var1) {
        //return this.list != null && this.list.size() > 0;
    	return true;
    }

    public abstract View getListView(int position, View convertView, ViewGroup viewGroup);

    /**
     * 当list为空([])时
     */
    public View getEmptyView(){
        return inflate( emptyId , null);
    }

    /**
     * 当list为空(null)时
     */
    public View getNullView(){
        return inflate(R.layout.item_null, null);
    }

    protected <T> T findViewHolder(View convertView, int layout, Class<T> clazz) {
        return findViewHolder(convertView,layout,clazz,false);
    }

    protected <T> T findViewHolder(View convertView, int layout, Class<T> clazz,boolean bool ) {
        Object var4;
        if(convertView == null) {
            convertView = this.inflate(layout,null);
            var4 = findView(context, convertView, clazz);
            convertView.setTag(var4);
        } else {
            var4 = convertView.getTag();
        }

        this.rowView = convertView;

        return ( T ) var4;
    }





    public static <T> T findView(Context var0, View var1, Class<T> var2) {
        try {
            String var3 = var0.getPackageName();
            //Log.e("findView","包名：" + var3);
            Object var4 = var2.newInstance();
            Field[] var10 = var2.getDeclaredFields();
            int var5 = var10.length;
            //Log.e("findView","包含控件数量：" + var5);

            for(int var6 = 0; var6 < var5; ++var6) {
                Field var7 = var10[var6];
                int var8 = var0.getResources().getIdentifier(var7.getName(), "id", var3);
                //Log.e("findView"," " + var8);
                var7.setAccessible(true);
                var7.set(var4, var1.findViewById(var8));
            }

            return ( T ) var4;
        } catch (Exception var9) {
            var9.printStackTrace();
            //Log.e("findView","catch");
            return null;
        }
    }

}
