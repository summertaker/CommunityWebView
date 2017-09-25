package com.summertaker.communitywebview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebFragment extends Fragment {

    private int mPosition;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mIsRefreshMode = false;

    private WebView mWebView;

    WebFragmentListener mCallback;

    // Container Activity must implement this interface
    public interface WebFragmentListener {
        public void onWebFragmentEvent(String event, String url, boolean canGoBack);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;

            // This makes sure that the container activity has implemented
            // the callback interface. If not, it throws an exception
            try {
                mCallback = (WebFragmentListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
            }
        }
    }

    public WebFragment() {
    }

    public static WebFragment newInstance(int position) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.web_fragment, container, false);

        mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3, R.color.swipe_color_4);

        mPosition = getArguments().getInt("position");
        //String title = getArguments().getString("title");
        String url = BaseApplication.getInstance().getSiteData(mPosition).getUrl();

        mWebView = rootView.findViewById(R.id.webView);
        //if (url.contains("theqoo.net")) {
        //    WebSettings webSettings = mWebView.getSettings();
        //    webSettings.setJavaScriptEnabled(true);
        //}

        mWebView.setWebViewClient(new WebViewClientClass());
        mWebView.loadUrl(url);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                mIsRefreshMode = true;
                refresh();
            }
        });
    }

    private void onRefreshComplete() {
        //Log.i(LOG_TAG, "onRefreshComplete");

        mSwipeRefreshLayout.setRefreshing(false);
        mIsRefreshMode = false;
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mCallback.onWebFragmentEvent("onPageStarted", mWebView.getUrl(), mWebView.canGoBack());
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mCallback.onWebFragmentEvent("onPageFinished", mWebView.getUrl(), mWebView.canGoBack());
            if (mIsRefreshMode) {
                onRefreshComplete();
            }
        }
    }

    public boolean goBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            return false;
        }
    }

    public void goTop() {
        mWebView.scrollTo(0, 0);
    }

    public void refresh() {
        //goTop();
        mWebView.reload();
    }

    public void openInNew() {
        String url = mWebView.getUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void share() {
        String title = mWebView.getTitle();
        String url = mWebView.getUrl();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(shareIntent, title));
    }
}