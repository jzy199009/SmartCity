package cn.jzy.gridlayout_test;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.jzy.gridlayout_test.view.DragGridLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private GridLayout mGridLayout;
    //记录拖拽的View
    private View dragView;
    //条目的矩形区域集合
    private List<Rect> rects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化GridLayout对象
        mGridLayout = (GridLayout) findViewById(R.id.gridLayout);

        //设置拖拽的监听
        mGridLayout.setOnDragListener(mOnDragListener);


        final DragGridLayout dragGridLayout = (DragGridLayout) findViewById(R.id.dragGridLayout);
        dragGridLayout.setCanDrag(true);
        List<String> items = new ArrayList<>();
        items.add("北京");
        items.add("上海");
        items.add("深圳");
        items.add("广州");
        items.add("武汉");
        items.add("济南");
        items.add("重庆");
        items.add("长沙");
        items.add("大连");
        items.add("南京");
        items.add("石家庄");
        dragGridLayout.setItems(items);

        final DragGridLayout dragGridLayout2 = (DragGridLayout) findViewById(R.id.dragGridLayout2);
        dragGridLayout2.setCanDrag(false);
        List<String> items2 = new ArrayList<>();
        items2.add("日本");
        items2.add("东京");
        items2.add("韩国");
        items2.add("新加坡");
        items2.add("纽约");
        items2.add("常德");
        items2.add("广西");
        items2.add("哈尔滨");
        items2.add("南昌");
        items2.add("无锡");
        items2.add("海南");
        dragGridLayout2.setItems(items2);

        //设置条目点击监听
        dragGridLayout.setOnDragItemClickListener(new DragGridLayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                //移除点击的条目，把条目添加到下面的Gridlayout
                dragGridLayout.removeView(tv);//移除是需要时间,不能直接添加
                dragGridLayout2.addItem(tv.getText().toString());
            }
        });

        dragGridLayout2.setOnDragItemClickListener(new DragGridLayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                //移除点击的条目，把条目添加到上面的Gridlayout
                dragGridLayout2.removeView(tv);//移除是需要时间,不能直接添加
                dragGridLayout.addItem(tv.getText().toString());
            }
        });
    }

    private View.OnDragListener mOnDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            //Log.i(TAG,getDragEventAction(event));
            if (dragView != null) {

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED://按下
                        dragView.setEnabled(false);
                        initRects();
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION://移动
                        //获取要交换条目的下标
                        int exchangeItemPosition = getExchangeItemPosition(event);
                        if (exchangeItemPosition != -1 && dragView != mGridLayout.getChildAt(exchangeItemPosition)) {
                            //移除
                            mGridLayout.removeView(dragView);
                            //添加
                            mGridLayout.addView(dragView,exchangeItemPosition);
                        }
                        break;
                    case DragEvent.ACTION_DRAG_ENDED://弹起
                        dragView.setEnabled(true);
                        break;
                    default:
                        break;
                }
            }
            return true;//拖拽事件成功的处理
        }
    };

    //获取要交换条目的下标
    private int getExchangeItemPosition(DragEvent event) {
        for (int i = 0; i < rects.size(); i++) {
            Rect rect = rects.get(i);
            if (rect.contains((int) event.getX(), (int) event.getY())) {
                return i;
            }
        }
        return -1;
    }

    //初始化条目的矩形区域
    private void initRects() {
        rects = new ArrayList<>();
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            View tv = mGridLayout.getChildAt(i);
            //创建矩形
            Rect rect = new Rect(tv.getLeft(), tv.getTop(), tv.getRight(), tv.getBottom());
            rects.add(rect);
        }
    }

    //SparseArray<String> 相当于hashmap,但更高效，谷歌官方推荐
    static SparseArray<String> dragEventType = new SparseArray<>();

    /*
    DragEvent.ACTION_DRAG_STARTED 手指按下
    DragEvent.ACTION_DRAG_ENTERED  手指按下|从GridLayout外部进入
    DragEvent.ACTION_DRAG_LOCATION 在Gridlayout的区间内移动
    DragEvent.ACTION_DRAG_EXITED   离开GridLayout的区间
    DragEvent.ACTION_DROP      松手
    DragEvent.ACTION_DRAG_ENDED  松手
     */
    static {
        dragEventType.put(DragEvent.ACTION_DRAG_STARTED, "STARTED");
        dragEventType.put(DragEvent.ACTION_DRAG_ENDED, "ENDED");
        dragEventType.put(DragEvent.ACTION_DRAG_ENTERED, "ENTERED");
        dragEventType.put(DragEvent.ACTION_DRAG_EXITED, "EXITED");
        dragEventType.put(DragEvent.ACTION_DRAG_LOCATION, "LOCATION");
        dragEventType.put(DragEvent.ACTION_DROP, "DROP");
    }

    public static String getDragEventAction(DragEvent de) {
        return dragEventType.get(de.getAction());
    }

    private int index = 0;
    private int margin = 8;

    public void addItem(View view) {
        //添加条目
        TextView textView = new TextView(this);
        //设置布局参数
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        //动态计算每个item的宽度
        params.width = getResources().getDisplayMetrics().widthPixels / 4 - 2 * margin;
        //设置外边距
        params.setMargins(margin, margin, margin, margin);
        textView.setLayoutParams(params);
        //设置内边距
        textView.setPadding(0, margin, 0, margin);
        textView.setGravity(Gravity.CENTER);
        textView.setText(index + "");
        //设置背景资源
        textView.setBackgroundResource(R.drawable.item_bg);
        index++;

        mGridLayout.addView(textView, 0);

        //给条目设置长按点击事件
        textView.setOnLongClickListener(mLongClickListener);
    }

    private View.OnLongClickListener mLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            dragView = v;
            //产生浮动的阴影效果
            //参数①传递数据  ② 产生阴影的    ③ 传递数据  ④ 状态
            v.startDrag(null, new View.DragShadowBuilder(v), null, 0);
            return true;//处理长按事件
        }
    };
}
