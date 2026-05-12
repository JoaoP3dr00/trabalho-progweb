package com.example.trabalho1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class BaleiaAdapter extends BaseAdapter {
    private Context mContext;
    private int[] mThumbIds = {
            R.drawable.baleia1, R.drawable.baleia4,
            R.drawable.baleia2, R.drawable.baleia3
    };

    public BaleiaAdapter(Context c) { mContext = c; }

    public int getCount() { return mThumbIds.length; }

    public Object getItem(int position) { return null; }

    public long getItemId(int position) { return 0; }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(400, 400));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }
}