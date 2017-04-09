package cn.jzy.gridlayout_test.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.jzy.gridlayout_test.R;

/**
 * Created by Administrator on 2017/4/7.
 * 自定义的可以拖拽的GridLayout
 */
public class DragGridLayout extends GridLayout {
    //列数
    private final int columnCount = 4;
    //是否可以拖拽
    private boolean canDrag;
    //记录拖拽的View
    private View dragView;
    //条目的矩形区域集合
    private List<Rect> rects;

    private List<String> items;
    //从外部设置条目的内容
    public void setItems(List<String> items) {
        this.items = items;
        createItems(items);
    }

    //创建所有的条目
    private void createItems(List<String> items) {
        for (String item : items) {
            addItem(item);
        }
    }

    public void setCanDrag(boolean canDrag) {
        this.canDrag = canDrag;
        if(canDrag){
            setOnDragListener(mOnDragListener);
        }else{
            setOnDragListener(null);
        }
    }

    public DragGridLayout(Context context) {
        this(context, null);
    }
    public DragGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public DragGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //android:columnCount="4"
        //android:animateLayoutChanges="true"
        //设置列数
        setColumnCount(columnCount);
        //开启动画
        setLayoutTransition(new LayoutTransition());
    }

    private View.OnDragListener mOnDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
           // Log.i("tag", "dragView != null============" + (dragView != null));
            if (dragView != null) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED://按下
                        dragView.setEnabled(false);
                        initRects();
                       // Log.i("tag", "ACTION_DRAG_STARTED============");
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION://移动
                        //获取要交换条目的下标
                        int exchangeItemPosition = getExchangeItemPosition(event);
                        //Log.i("tag", "exchangeItemPosition: " + exchangeItemPosition + "  ===  " + (dragView != getChildAt(exchangeItemPosition)));
                        if (exchangeItemPosition != -1 && dragView != getChildAt(exchangeItemPosition)) {
                           // Log.i("tag", "交换位置");
                            //移除
                            removeView(dragView);
                            //添加
                            addView(dragView, exchangeItemPosition);
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
        for (int i = 0; i < getChildCount(); i++) {
            View tv = getChildAt(i);
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

    private int margin = 8;

    public void addItem(String content) {
        //添加条目
        TextView textView = new TextView(getContext());
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
        textView.setText(content);
        //设置背景资源
        textView.setBackgroundResource(R.drawable.item_bg);
        addView(textView, 0);

        if(canDrag){
            //给条目设置长按点击事件
            textView.setOnLongClickListener(mLongClickListener);
        }else{
            //给条目设置长按点击事件(无)
            textView.setOnLongClickListener(null);
        }

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //接口回调
                if (mOnDragItemClickListener != null) {
                    mOnDragItemClickListener.onDragItemClick((TextView) v);
                }
            }
        });
    }

    private View.OnLongClickListener mLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            dragView = v;
            //产生浮动的阴影效果
            //参数①传递数据  ② 产生阴影的    ③ 传递数据  ④ 状态
            v.startDrag(null, new View.DragShadowBuilder(v), null, 0);
            //Log.i("tag", "长按事件==============");
            return true;//处理长按事件
        }
    };

    private OnDragItemClickListener mOnDragItemClickListener;

    public void setOnDragItemClickListener(OnDragItemClickListener onDragItemClickListener) {
        mOnDragItemClickListener = onDragItemClickListener;
    }

    //自定义接口
    public interface OnDragItemClickListener{
        void onDragItemClick(TextView tv);
    }
}
