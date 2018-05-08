package myutil.xhh.com.webapp.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.TbsVideo;
import com.tencent.smtt.sdk.VideoActivity;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import myutil.xhh.com.webapp.R;

public class ProgressWebview extends WebView {
    private ProgressBar progressbar;  //进度条
    private Context mContext;

    private int progressHeight = 10;  //进度条的高度，默认10px


    public ProgressWebview(Context context) {
        super(context);
        initView(context);
    }

    public ProgressWebview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);

    }

    private void initView(Context context) {
        mContext=context;
        //开启js脚本支持
        getSettings().setJavaScriptEnabled(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getSettings().setDefaultTextEncodingName("utf-8");
        //创建进度条
        progressbar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleLarge);
//        //设置加载进度条的高度
//        progressbar.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));

        Drawable drawable = context.getResources().getDrawable(R.drawable.progress_bar_states);
        progressbar.setProgressDrawable(drawable);

        //添加进度到WebView
        addView(progressbar,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT,Gravity.CENTER));

        //适配手机大小
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        getSettings().setBuiltInZoomControls(true);
        getSettings().setDisplayZoomControls(false);
        getSettings().setUseWideViewPort(true);//设定支持viewport
        getSettings().setLoadWithOverviewMode(true);//是否允许WebView度超出以概览的方式载入页面
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        getSettings().setDomStorageEnabled(true);//开启WebView的缓存功能可以减少对服务器资源的请求
        getSettings().setSupportZoom(true);//WebView是否支持使用屏幕上的缩放控件和手势进行缩放
        getSettings().setSavePassword(true);
        getSettings().setSaveFormData(true);// 保存表单数据
        getSettings().setSupportMultipleWindows(true);// 新加



        setDrawingCacheEnabled(true);

        setWebChromeClient(new WVChromeClient());
        setWebViewClient(new WVClient());
    }


    //进度显示
    private class WVChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }

            if (mListener != null) {
                mListener.onProgressChange(view, newProgress);
            }

            super.onProgressChanged(view, newProgress);
        }

    }

    private class WVClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //在当前Activity打开
            Log.e("视频", "shouldOverrideUrlLoading: "+url);
            if (url.contains("video")){
                if(TbsVideo.canUseTbsPlayer(mContext)) {
                    TbsVideo.openVideo(mContext, url);
                }
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //https忽略证书问题
            handler.proceed();
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            progressbar.setVisibility(GONE);
            if (mListener != null) {
                mListener.onPageFinish(view);
            }
            super.onPageFinished(view, url);

        }

    }

    private onWebViewListener mListener;

    public void setOnWebViewListener(onWebViewListener listener) {
        this.mListener = listener;
    }

    //进度回调接口
    public interface onWebViewListener {
        void onProgressChange(WebView view, int newProgress);

        void onPageFinish(WebView view);
    }
}
