package com.couse.online.kaowneaw.liveat500px.managers;

import android.content.Context;

import com.couse.online.kaowneaw.liveat500px.dao.PhotoItemCollectionDAO;
import com.couse.online.kaowneaw.liveat500px.dao.PhotoItemDAO;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.util.ArrayList;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class PhotoListManager {

    private PhotoItemCollectionDAO dao;

    public PhotoItemCollectionDAO getDao() {
        return dao;
    }

    public void setDao(PhotoItemCollectionDAO dao) {
        this.dao = dao;
    }

    public void appendDaoAtTopPosition(PhotoItemCollectionDAO newDao) {
        if (this.dao == null) {
            dao = new PhotoItemCollectionDAO();
        }
        if (this.dao.getData() == null) {
            this.dao.setData(new ArrayList<PhotoItemDAO>());
        }

        dao.getData().addAll(0, newDao.getData());
    }

    public void appendDaoAtBottomPosition(PhotoItemCollectionDAO newDao) {
        if (this.dao == null) {
            dao = new PhotoItemCollectionDAO();
        }
        if (this.dao.getData() == null) {
            this.dao.setData(new ArrayList<PhotoItemDAO>());
        }

        dao.getData().addAll(dao.getData().size(), newDao.getData());
    }

    public int getMaximumId() {
        int maxId = 0;
        if (this.dao == null) {
            return maxId;
        }
        if (dao.getData() == null) {
            return maxId;
        }

        maxId = dao.getData().get(0).getId();

        for (int i = 1; i < dao.getData().size(); i++) {
            maxId = Math.max(maxId, dao.getData().get(i).getId());
        }

        return maxId;
    }

    public int getMinimumId() {
        int minId = 0;
        if (this.dao == null) {
            return minId;
        }
        if (dao.getData() == null) {
            return minId;
        }

        minId = dao.getData().get(0).getId();

        for (int i = 1; i < dao.getData().size(); i++) {
            minId = Math.min(minId, dao.getData().get(i).getId());
        }

        return minId;
    }

    public int getCount() {
        if (this.dao == null) {
            return 0;
        }
        if (dao.getData() == null) {
            return 0;
        }
        return dao.getData().size();
    }
}
