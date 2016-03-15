package com.estasvegano.android.estasvegano.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.estasvegano.android.estasvegano.R;
import com.estasvegano.android.estasvegano.entity.ProductType;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.estasvegano.android.estasvegano.view.util.Utils.getTypeImage;
import static com.estasvegano.android.estasvegano.view.util.Utils.getTypeTitle;

public class ProductTypeArrayAdapter extends BaseAdapter {

    @NonNull
    private final Context context;

    public ProductTypeArrayAdapter(@NonNull Context context) {
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return ProductType.values().length;
    }

    @Override
    @NonNull
    public ProductType getItem(int position) {
        return ProductType.values()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_product_type, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(getTypeTitle(getItem(position)));
        holder.imageView.setImageResource(getTypeImage(getItem(position)));

        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.item_product_type_image)
        ImageView imageView;

        @Bind(R.id.item_product_type_title)
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
