package com.couse.online.kaowneaw.liveat500px.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.couse.online.kaowneaw.liveat500px.R;
import com.couse.online.kaowneaw.liveat500px.dao.PhotoItemCollectionDAO;
import com.couse.online.kaowneaw.liveat500px.dao.PhotoItemDAO;
import com.couse.online.kaowneaw.liveat500px.views.PhotoListItem;

/**
 * Created by Kaowneaw on 4/17/2017.
 */

public class PhotoListAdapter extends BaseAdapter {

    PhotoItemCollectionDAO dao;
    private int lastPosition = -1;

    public void setDao(PhotoItemCollectionDAO dao) {
        this.dao = dao;
    }

    @Override
    public int getCount() {
        if (this.dao == null) return 1;
        if (this.dao.getData() == null) return 1;
        return this.dao.getData().size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return this.dao.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getCount() - 1 ? 1 : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position == getCount() - 1) {
            ProgressBar progressBar;
            if (convertView != null) {
                progressBar = (ProgressBar) convertView;
            } else {
                progressBar = new ProgressBar(parent.getContext());
            }
            return progressBar;
        }

        PhotoListItem item;

        if (convertView != null) item = (PhotoListItem) convertView;
        else item = new PhotoListItem(parent.getContext());

        PhotoItemDAO dao = (PhotoItemDAO) getItem(position);
        item.setTitle(dao.getCaption());
        item.setDesc(dao.getUsername() + "\n" + dao.getCamera());
        item.setImgUrl(dao.getImgUrl());

        if (position > lastPosition) {
            Animation anim = AnimationUtils.loadAnimation(parent.getContext(), R.anim.up_from_bottom);
            item.startAnimation(anim);
            lastPosition = position;
        }

        return item;
    }

    public void increaseLastPosition(int amount) {
        lastPosition += amount;
    }
}
