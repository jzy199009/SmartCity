package cn.jzy.a360news;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzy.a360news.adapter.NewsAdapter;
import cn.jzy.a360news.bean.ResuleBean;
import cn.jzy.a360news.view.RecycleViewDivider;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }

        ButterKnife.bind(this);

        //RecyclerView的初始化
        //设置线性的布局管理器
        mRv.setLayoutManager(new LinearLayoutManager(this));
        //设置分割线
        mRv.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,1, Color.BLUE));

        String url = "http://10.0.2.2:8080/360/list1.json";

        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //将结果数据转换为javaBean对象
                        ResuleBean bean = new Gson().fromJson(response, ResuleBean.class);
                        //设置适配器
                        mRv.setAdapter(new NewsAdapter(bean.data,getApplicationContext()));
                    }
                });

    }
}
