package com.xiuxiu.confinement_nurse.help.activity;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * <p>
 * <h2>简述:</h2>
 * <ol>无</ol>
 * <h2>功能描述:</h2>
 * <ol>无</ol>
 * <h2>修改历史:</h2>
 * <ol>无</ol>
 * </p>
 *
 * @author 11925
 * @date 2018/8/14/014
 */
public class ActivityResultRequest {

    private OnActResultEventDispatcherFragment fragment;

    public ActivityResultRequest(FragmentActivity activity) {
        fragment = getEventDispatchFragment(activity);
    }

    private OnActResultEventDispatcherFragment getEventDispatchFragment(FragmentActivity activity) {
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();

        OnActResultEventDispatcherFragment fragment = findEventDispatchFragment(fragmentManager);
        if (fragment == null) {
            fragment = new OnActResultEventDispatcherFragment();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, OnActResultEventDispatcherFragment.TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    private OnActResultEventDispatcherFragment findEventDispatchFragment(FragmentManager manager) {
        return (OnActResultEventDispatcherFragment) manager.findFragmentByTag(OnActResultEventDispatcherFragment.TAG);
    }

    public void startForResult(Intent intent, Callback callback) {
        fragment.startForResult(intent, callback);
    }

    public interface Callback {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
