package myutil.xhh.com.webapp;

import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.tencent.smtt.sdk.WebView;

import myutil.xhh.com.webapp.view.ProgressWebview;

public class MainActivity extends AppCompatActivity {
    private ProgressWebview mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        initView();
        initData();
    }

    private void initData() {
        mWebView.loadUrl("http://www.baidu.com");//在这里填写你自己的Url就可以了
    }

    private void initView() {
        mWebView=findViewById(R.id.twv_my);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mWebView.canGoBack()){
                mWebView.goBack();
                return false;
            }else {
                return super.onKeyDown(keyCode, event);
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
