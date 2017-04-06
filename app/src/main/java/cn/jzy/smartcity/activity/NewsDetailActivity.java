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

import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzy.smartcity.R;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
                showShare();
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

    int selectPosition = 2;
    //修改网页字体大小
    private void changeWebViewTextSize() {
        //单选的对话框   AlertDialog
        //设置字体大小 webView.getSettings().setTextSize(TextS);
        new AlertDialog.Builder(this)
                .setTitle("选择字体的大小")
                .setSingleChoiceItems(types, selectPosition, new DialogInterface.OnClickListener() {
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

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("镇压新闻");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是镇压新闻分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    //友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
