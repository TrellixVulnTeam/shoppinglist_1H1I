package com.pandian.samuvel.shoppinglist.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pandian.samuvel.shoppinglist.Helper;
import com.pandian.samuvel.shoppinglist.Model.ShoppingItemList;
import com.pandian.samuvel.shoppinglist.R;

import java.util.ArrayList;

/**
 * Created by samuvelp on 26/11/17.
 */

public class ListItemAdapter extends BaseAdapter {
    ArrayList<ShoppingItemList> list = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    String groupKey;
    AlertDialog removeDialog;

    public ListItemAdapter(ArrayList<ShoppingItemList> list, Context context,String groupKey) {
        this.list = list;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupKey = groupKey;
    }
    class Holder{
        TextView itemName;
        ImageButton deleteListButton;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View rowView = inflater.inflate(R.layout.rowview_list_item,null);
        Holder holder = new Holder();
        holder.itemName = rowView.findViewById(R.id.list_item_name_textView);
        holder.deleteListButton = rowView.findViewById(R.id.list_item_delete_button);
        holder.itemName.setText(list.get(i).getItemName());
        holder.deleteListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRemoveListPrompt(i);
            }
        });
        return rowView;
    }
    private void showRemoveListPrompt(final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setMessage("Are you sure you want to remove this list?");
        builder.setTitle("Remove List");
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Helper.removeListItem(groupKey,list.get(pos).getKey());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        removeDialog = builder.create();
        removeDialog.show();
    }
}
