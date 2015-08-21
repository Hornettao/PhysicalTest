package com.example.hornettao.physicaltest2;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hornettao.tab.TabHostActivity;
import com.example.hornettao.tab.TabItem;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>整个流程就像使用ListView自定BaseAdapter一样</p>
 *
 * <p>如果要自定义TabHostActivity的Theme，并且不想要头部阴影
 * 一定要添加这个android:windowContentOverlay = null</p>
 *
 * <p>如果想在别的项目里面使用TabHostActivity
 * 可以项目的属性里面找到Android，然后在Library部分添加这个项目(Api)
 * <a href="http://www.cnblogs.com/qianxudetianxia/archive/2011/05/01/2030232.html">如何添加</a></p>
 * */
public class MainActivity extends TabHostActivity {

    List<TabItem> mItems;
    private LayoutInflater mLayoutInflater;

    /**在初始化TabWidget前调用
     * 和TabWidget有关的必须在这里初始化*/
    @Override
    protected void prepare() {
        TabItem home = new TabItem(
                "预约",									// title
                R.drawable.reserve,					// icon
                R.drawable.main_tab_item_bg,			// background
                new Intent(this, ReserveActivity.class));	// intent

        TabItem info = new TabItem(
                "查看通知",
                R.drawable.announcement,
                R.drawable.main_tab_item_bg,
                new Intent(this, SeeAnnouncement.class));

        TabItem msg = new TabItem(
                "查看结果",
                R.drawable.result,
                R.drawable.main_tab_item_bg,
                new Intent(this, SeeResultActivity.class));

        TabItem square = new TabItem(
                "我的账户",
                R.drawable.account,
                R.drawable.main_tab_item_bg,
                new Intent(this, MyAccountActivity.class));



        mItems = new ArrayList<TabItem>();
        mItems.add(home);
        mItems.add(info);
        mItems.add(msg);
        mItems.add(square);

        // 设置分割线
        TabWidget tabWidget = getTabWidget();
        tabWidget.setDividerDrawable(R.drawable.tab_divider);

        mLayoutInflater = getLayoutInflater();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentTab(0);
        System.out.println("--->main on create");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("--->main on start");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("--->main on start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("--->main on resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("--->main on pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("--->main on stop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("--->main on destroy");
    }

    /**tab的title，icon，边距设定等等*/
    @Override
    protected void setTabItemTextView(TextView textView, int position) {
        textView.setPadding(3, 3, 3, 3);
        textView.setText(mItems.get(position).getTitle());
        textView.setBackgroundResource(mItems.get(position).getBg());
        textView.setCompoundDrawablesWithIntrinsicBounds(0, mItems.get(position).getIcon(), 0, 0);

    }

    /**tab唯一的id*/
    @Override
    protected String getTabItemId(int position) {
        return mItems.get(position).getTitle();	// 我们使用title来作为id，你也可以自定
    }

    /**点击tab时触发的事件*/
    @Override
    protected Intent getTabItemIntent(int position) {
        return mItems.get(position).getIntent();
    }

    @Override
    protected int getTabItemCount() {
        return mItems.size();
    }

//    /**自定义头部文件*/
//    @Override
//    protected View getTop() {
//        return mLayoutInflater.inflate(R.layout.main_top, null);
//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //System.out.println("--->Main back");
            //finish();
            Toast.makeText(MainActivity.this, "text", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyDown(keyCode, event);
    }

}