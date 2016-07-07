package com.lgl.systemtools.utils;

/*
 *  项目名：  Okl_SystemTools
 *  包名：    com.okl.okl_systemtools.utils
 *  文件名:   DialogUtils
 *  创建者:   LGL
 *  创建时间:  2016/7/7 14:20
 *  描述：    原生Dialog封装的工具类
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

public class DialogUtils {

    /**
     * 标准的提示框
     *
     * @param mContext    上下文
     * @param title       标题
     * @param message     内容
     * @param pButtonName 按钮名字
     * @param Plistener   监听事件
     * @param nButtonName 按钮名字
     * @param Nlistener   监听事件
     */
    public static void showDialog(Context mContext, String title, String message, String pButtonName,
                                  DialogInterface.OnClickListener Plistener, String nButtonName,
                                  DialogInterface.OnClickListener Nlistener) {
        //创建对象
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //设置标题
        builder.setTitle(title);
        //设置内容
        builder.setMessage(message);
        //确定按钮
        builder.setPositiveButton(pButtonName, Plistener);
        //返回按钮
        builder.setNegativeButton(nButtonName, Nlistener);
        //显示
        builder.show();
    }

    /**
     * 有icon的dialog
     *
     * @param mContext    上下文
     * @param drawable    icon
     * @param title       标题
     * @param message     内容
     * @param pButtonName 按钮名字
     * @param Plistener   监听事件
     * @param nButtonName 按钮名字
     * @param Nlistener   监听事件
     */
    public static void showIconDialog(Context mContext, Drawable drawable, String title, String message, String pButtonName,
                                      DialogInterface.OnClickListener Plistener, String nButtonName,
                                      DialogInterface.OnClickListener Nlistener) {
        //创建对象
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //设置icon
        builder.setIcon(drawable);
        //设置标题
        builder.setTitle(title);
        //设置内容
        builder.setMessage(message);
        //确定按钮
        builder.setPositiveButton(pButtonName, Plistener);
        //返回按钮
        builder.setNegativeButton(nButtonName, Nlistener);
        //显示
        builder.show();
    }

    /**
     * 没有按钮的提示框
     *
     * @param mContext 上下文
     * @param title    标题
     * @param message  内容
     */
    public static void showNoButtonDialog(Context mContext, String title, String message) {
        //创建对象
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //设置标题
        builder.setTitle(title);
        //设置内容
        builder.setMessage(message);
        //显示
        builder.show();
    }


    /**
     * 只有一个按钮的提示框
     * @param mContext
     * @param drawable
     * @param title
     * @param message
     * @param pButtonName
     * @param Plistener
     */
    public void showOnlyButtonDialog(Context mContext, Drawable drawable, String title, String message, String pButtonName,
                                     DialogInterface.OnClickListener Plistener) {
        //创建对象
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //设置icon
        builder.setIcon(drawable);
        //设置标题
        builder.setTitle(title);
        //设置内容
        builder.setMessage(message);
        //确定按钮
        builder.setPositiveButton(pButtonName, Plistener);
        //显示
        builder.show();
    }

}
