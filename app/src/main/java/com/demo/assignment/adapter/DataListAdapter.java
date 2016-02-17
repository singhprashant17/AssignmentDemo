package com.demo.assignment.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.demo.assignment.R;
import com.demo.assignment.model.DataModel;

import java.util.List;

public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.ViewHolder> {

    private Context context;
    private List<DataModel> items;
    private ImageLoader imageLoader;

    public DataListAdapter(Context context, List<DataModel> items) {
        this.context = context;
        this.items = items;
        imageLoader = getImageLoader();
    }

    @NonNull
    private ImageLoader getImageLoader() {
        return new ImageLoader(Volley.newRequestQueue(context), new ImageLoader
                .ImageCache() {
            private LruCache<String, Bitmap> mCache = new LruCache<>(getDefaultLruCacheSize());

            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }

            public int getDefaultLruCacheSize() {
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                return maxMemory / 8;
            }

            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.data_item_list, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataModel dataModel = items.get(position);
        holder.txtId.setText(dataModel.getId());
        if (TextUtils.isEmpty(dataModel.getTitle())) {
            holder.txtTitle.setVisibility(View.GONE);
        } else {
            holder.txtTitle.setVisibility(View.VISIBLE);
            holder.txtTitle.setText(dataModel.getTitle());
        }
        holder.imgPic.setDefaultImageResId(R.mipmap.ic_launcher);
        holder.imgPic.setImageUrl(dataModel.getUri(), imageLoader);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtId;
        public NetworkImageView imgPic;
        public TextView txtTitle;

        public ViewHolder(final View itemView) {
            super(itemView);
            txtId = (TextView) itemView.findViewById(R.id.txtId);
            imgPic = (NetworkImageView) itemView.findViewById(R.id.imgPic);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        }
    }
}