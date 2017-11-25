package com.pandian.samuvel.shoppinglist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pandian.samuvel.shoppinglist.Model.ShoppingList;
import com.pandian.samuvel.shoppinglist.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by HP PC on 31-10-2017.
 */

public class ShoppingListAdapter extends BaseAdapter {

    ArrayList<ShoppingList> list = new ArrayList<>();
    Context context;
    private static LayoutInflater inflater;

    public ShoppingListAdapter(ArrayList<ShoppingList> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class Holder{
        TextView listNameTv,ownerNameTv,createdTimeTv;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View rowView = inflater.inflate(R.layout.rowview_main_shoppinglist,null);
        Holder holder = new Holder();
        holder.listNameTv = rowView.findViewById(R.id.listNameTv);
        holder.ownerNameTv = rowView.findViewById(R.id.ownerNameTv);
        holder.createdTimeTv = rowView.findViewById(R.id.createdTimeTv);
        holder.listNameTv.setText(list.get(position).getListName());
        holder.ownerNameTv.setText(list.get(position).getOwnerName());

        Date date = new Date(list.get(position).getTimeStampCreated().get("timeStamp"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String time = simpleDateFormat.format(date);
        holder.createdTimeTv.setText(time);
        return rowView;
    }
}
