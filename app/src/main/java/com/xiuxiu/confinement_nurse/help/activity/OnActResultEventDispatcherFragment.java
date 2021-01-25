package com.xiuxiu.confinement_nurse.help.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol>onActivityResult 的快速返回</ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/8/14/014
 */
public class OnActResultEventDispatcherFragment extends Fragment {
    public static final int ActivityNotFoundCode = -404;
    public static final String TAG = "on_act_result_event_dispatcher";

    private SparseArray<ActivityResultRequest.Callback> mCallbacks = new SparseArray<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startForResult(Intent intent, ActivityResultRequest.Callback callback) {
        int min = 1;
        int index = (int) (Math.random() * (100 - min) + min);
        mCallbacks.put(index, callback);
        try {
            startActivityForResult(intent, index);
        } catch (ActivityNotFoundException e) {
            callback.onActivityResult(index, ActivityNotFoundCode, null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultRequest.Callback callback = mCallbacks.get(requestCode);
        if (callback != null) {
            callback.onActivityResult(callback.hashCode(), resultCode, data);
        }
        mCallbacks.remove(requestCode);
    }

}

