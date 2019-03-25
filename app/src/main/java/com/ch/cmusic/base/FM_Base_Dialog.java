package com.ch.cmusic.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者： ch
 * 时间： 2018/8/25 0025-上午 11:30
 * 描述：
 * 来源：
 */


public abstract class FM_Base_Dialog extends BottomSheetDialogFragment {

    private View rootView;
    protected Unbinder unbinder;

    protected abstract int getLayoutId();

    public Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);

        initView();
        addListener();
        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
//        fixHeight();
    }

    /**
     * 通过资源res获得view
     *
     * @param res
     * @return
     */
    public View getViewByRes(@LayoutRes int res) {
        return LayoutInflater.from(context).inflate(res, null);
    }

    protected void addListener() {

    }

    protected void initView() {

    }

    /**
     * 获得TextView 的文本
     *
     * @param tv
     * @return
     */
    public String getTV(TextView tv) {
        return tv == null ? "" : tv.getText().toString().trim();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            //处理dialog 背景
            ViewGroup parent = (ViewGroup) rootView.getParent();
            parent.setBackgroundResource(android.R.color.transparent);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 打开指定的activity
     *
     * @param cls
     */
    public void startA(@NonNull Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }


    /**
     * toast
     *
     * @param msg
     */
    public void showtoast(@NonNull String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }


    public void fixHeight() {
        if (null == rootView) {
            return;
        }

        View parent = (View) rootView.getParent();
        try {
            BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
            rootView.measure(0, 0);
            behavior.setPeekHeight(rootView.getMeasuredHeight());

            if (parent.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {

                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
                params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                parent.setLayoutParams(params);
            } else if (parent.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) parent.getLayoutParams();
                params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                parent.setLayoutParams(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
