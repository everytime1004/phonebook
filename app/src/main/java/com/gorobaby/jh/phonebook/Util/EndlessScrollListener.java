package com.gorobaby.jh.phonebook.Util;

import android.content.Context;
import android.util.Log;
import android.widget.AbsListView;

/**
 * Created by love on 2016-05-31.
 */
public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int mBufferItemCount = 10;
    private int mCurrentPage = 0;
    private int mItemCount = 0;
    private boolean mIsLoading = true;
    private Context mContext;

    public EndlessScrollListener(int bufferItemCount, Context context) {
        this.mBufferItemCount = bufferItemCount;
        this.mContext = context;
    }

    public abstract void loadMore(int page, int totalItemsCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d("onScrollStateChanged", String.valueOf(scrollState));
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        if (totalItemCount < mItemCount) {
            this.mItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.mIsLoading = true; }
        }

        if (mIsLoading && (totalItemCount > mItemCount)) {
            mIsLoading = false;
            mItemCount = totalItemCount;
            mCurrentPage++;
        }

        if (!mIsLoading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + mBufferItemCount)) {
                loadMore(mCurrentPage + 1, totalItemCount);
                mIsLoading = true;
        }
    }
}
