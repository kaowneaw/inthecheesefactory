package com.couse.online.kaowneaw.liveat500px.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.couse.online.kaowneaw.liveat500px.R;
import com.couse.online.kaowneaw.liveat500px.adapter.PhotoListAdapter;
import com.couse.online.kaowneaw.liveat500px.dao.PhotoItemCollectionDAO;
import com.couse.online.kaowneaw.liveat500px.managers.HttpManager;
import com.couse.online.kaowneaw.liveat500px.managers.PhotoListManager;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class MainFragment extends Fragment {

    ListView listView;
    PhotoListAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    PhotoListManager photoListManager;
    Button newPhotosBtn;
    boolean isLoadingMore = false;

    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize fragment level
        photoListManager = new PhotoListManager();

        if (savedInstanceState != null)
            onRestoreInstanceStat(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(pullRefreshListener);

        adapter = new PhotoListAdapter();
        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(scrollListener);

        newPhotosBtn = (Button) rootView.findViewById(R.id.newPhotosBtn);
        newPhotosBtn.setOnClickListener(btnClickListener);

        refreshData();
    }

    private void refreshData() {
        if (photoListManager.getCount() == 0) {
            loadData();
        } else {
            loadNewerData(photoListManager.getMaximumId());
        }
    }

    private void loadData() {
        Call<PhotoItemCollectionDAO> call = HttpManager.getInstance().getService().loadPhotoList();
        call.enqueue(new PhotoListLoadCallBack(PhotoListLoadCallBack.MODE_RELOAD));
    }

    private void loadNewerData(int maxId) {
        Call<PhotoItemCollectionDAO> call = HttpManager.getInstance().getService().loadPhotoListAfterId(maxId);
        call.enqueue(new PhotoListLoadCallBack(PhotoListLoadCallBack.MODE_RELOAD_NEW));
    }

    private void loadMoreData() {
        if (isLoadingMore) return;
        isLoadingMore = true;
        int minId = photoListManager.getMinimumId();
        Call<PhotoItemCollectionDAO> call = HttpManager.getInstance().getService().loadPhotoListBeforeId(minId);
        call.enqueue(new PhotoListLoadCallBack(PhotoListLoadCallBack.MODE_RELOAD_MORE));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
        //// TODO: 5/10/2017  save photolistmanager
    }

    /*
     * Restore Instance State Here
     */

    private void onRestoreInstanceStat(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

    private void showBtnNewPhotosBtn() {
        newPhotosBtn.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.zoom_fade_in);
        newPhotosBtn.startAnimation(anim);
    }

    private void hiddenBtnNewPhotosBtn() {
        newPhotosBtn.setVisibility(View.GONE);
        Animation anim = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.zoom_fade_out);
        newPhotosBtn.startAnimation(anim);
    }

    private void showToast(String text) {
        Toast.makeText(Contextor.getInstance().getContext(), text, Toast.LENGTH_SHORT).show();
    }


    final View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listView.smoothScrollToPosition(0);
            hiddenBtnNewPhotosBtn();
        }
    };

    final SwipeRefreshLayout.OnRefreshListener pullRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };

    final AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            swipeRefreshLayout.setEnabled(firstVisibleItem == 0);
            if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                if (photoListManager.getCount() > 0) {
                    // load more
                    loadMoreData();
                }
            }
        }
    };


    private class PhotoListLoadCallBack implements Callback<PhotoItemCollectionDAO> {

        static final int MODE_RELOAD = 0;
        static final int MODE_RELOAD_NEW = 1;
        static final int MODE_RELOAD_MORE = 3;

        int mode;

        PhotoListLoadCallBack(int mode) {
            this.mode = mode;
        }

        @Override
        public void onResponse(Call<PhotoItemCollectionDAO> call, Response<PhotoItemCollectionDAO> response) {
            if (response.isSuccessful()) {
                PhotoItemCollectionDAO dao = response.body();

                int firstVisible = listView.getFirstVisiblePosition();
                View c = listView.getChildAt(0);
                int top = c == null ? 0 : c.getTop();

                if (mode == MODE_RELOAD_NEW) {

                    photoListManager.appendDaoAtTopPosition(dao);
                } else if (mode == MODE_RELOAD_MORE) {

                    photoListManager.appendDaoAtBottomPosition(dao);
                    clearLoadingMoreFlagIfCapable(mode);
                } else {

                    photoListManager.setDao(dao);
                }

                adapter.setDao(photoListManager.getDao());
                adapter.notifyDataSetChanged();

                if (mode == MODE_RELOAD_NEW) {
                    // SET Position ListView
                    int additionalSize = (dao != null && dao.getData() != null) ? dao.getData().size() : 0;
                    adapter.increaseLastPosition(additionalSize);
                    listView.setSelectionFromTop(firstVisible + additionalSize, top);

                    if (additionalSize > 0) {
                        showBtnNewPhotosBtn();
                    }
                }

                showToast("Loading Complete " + dao.getData().size());

            } else {
                clearLoadingMoreFlagIfCapable(mode);

                try {

                    showToast(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onFailure(Call<PhotoItemCollectionDAO> call, Throwable t) {
            clearLoadingMoreFlagIfCapable(mode);
            swipeRefreshLayout.setRefreshing(false);
            showToast(t.toString());
        }

        private void clearLoadingMoreFlagIfCapable(int mode) {
            if (mode == MODE_RELOAD_MORE) isLoadingMore = false;
        }
    }
}
