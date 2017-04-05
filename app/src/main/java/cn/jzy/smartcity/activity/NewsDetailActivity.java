package cn.jzy.smartcity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzy.smartcity.R;

/**
 * Created by Administrator on 2017/4/6.
 */
public class NewsDetailActivity extends Activity {

    @BindView(R.id.ib_back)
    ImageButton mIbBack;
    @BindView(R.id.ib_textsize)
    ImageButton mIbTextsize;
    @BindView(R.id.ib_share)
    ImageButton mIbShare;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.pb)
    ProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        String url = getIntent().getStringExtra("url");

        //让WebView支持javaScript,加载全文功能
        mWebView.getSettings().setJavaScriptEnabled(true);

        //设置WebView的客户端
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                //WebView加载网页结束，隐藏进度条
                mPb.setVisibility(View.INVISIBLE);
            }
        });

        //让WebView去加载网页
        mWebView.loadUrl(url);

    }

    @OnClick({R.id.ib_back, R.id.ib_textsize, R.id.ib_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_textsize:
                changeWebViewTextSize();
                break;
            case R.id.ib_share:
                break;
        }
    }

    private String[] types = new String[]{
            "超大号字体",
            "大号字体",
            "正常字体",
            "小号字体",
            "超小号字体",
    };

    private WebSettings.TextSize[] textSizes = new WebSettings.TextSize[]{
            WebSettings.TextSize.LARGEST,
            WebSettings.TextSize.LARGER,
            WebSettings.TextSize.NORMAL,
            WebSettings.TextSize.SMALLER,
            WebSettings.TextSize.SMALLEST,
    };

    int selectPosition;
    //修改网页字体大小
    private void changeWebViewTextSize() {
        //单选的对话框   AlertDialog
        //设置字体大小 webView.getSettings().setTextSize(TextS);
        new AlertDialog.Builder(this)
                .setTitle("选择字体的大小")
                .setSingleChoiceItems(types, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectPosition = which;
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mWebView.getSettings().setTextSize(textSizes[selectPosition]);
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }
}
