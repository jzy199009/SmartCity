package cn.jzy.a360news;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/8.
 */
public class NewsDetailActivity extends Activity {
    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        //初始化WebView
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //给网页里面的img标签去添加点击事件
                addPictureClick();
            }
        });

        //mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);

        String url = getIntent().getStringExtra("url");
        //加载网页
        mWebView.loadUrl(url);

        //传递对象给webview
        mWebView.addJavascriptInterface(new JsCallJava() {
            @JavascriptInterface
            @Override
            public void openImage(String src) {
                Intent intent = new Intent(getApplicationContext(),ShowActivity.class);
                intent.putExtra("url",src);
                startActivity(intent);
            }
        },"imagelistner");
    }

    private void addPictureClick() {
        //android调用js代码
        mWebView.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName(\"img\"); "
                + "for(var i=0;i<objs.length;i++)  " + "{"
                + "    objs[i].onclick=function()  " + "   " + " {  "
                + "        window.imagelistner.openImage(this.src);  "
                + "    }  " + "}" + "})()");
    }

    public interface JsCallJava{
        public void openImage(String src);
    }
}
