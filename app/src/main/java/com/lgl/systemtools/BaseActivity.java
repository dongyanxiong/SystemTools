package com.lgl.systemtools;

/*
 *  项目名：  SystemTools 
 *  包名：    com.lgl.systemtools
 *  文件名:   BaseActivity
 *  创建者:   LGL
 *  创建时间:  2016/7/7 16:41
 *  描述：    Activity基类
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ActionBar上显示返回
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 菜单操作
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
