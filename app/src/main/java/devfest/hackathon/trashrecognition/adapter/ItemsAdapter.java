package devfest.hackathon.trashrecognition.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import devfest.hackathon.trashrecognition.R;
import devfest.hackathon.trashrecognition.common.Item;

public class ItemsAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<Item> itemList;


    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        private int imageItem;
        private String tilteItem;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=LayoutInflater.from(context).inflate(R.layout.display_each_item, null);



        return convertView;
    }
}
