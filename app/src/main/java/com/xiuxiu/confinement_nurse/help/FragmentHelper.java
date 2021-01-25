package com.xiuxiu.confinement_nurse.help;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.utlis.FragmentUtils;


public final class FragmentHelper {
    private FragmentHelper() {
    }

    public synchronized static void showCurrentFragment(FragmentManager fragmentActivity, String tag, FragmentCallBack ifCreateFragment) {
        showCurrentFragment(fragmentActivity, tag, false, ifCreateFragment);
    }

    public synchronized static void showCurrentFragment(FragmentActivity fragmentActivity, String tag, boolean isAddStack, FragmentCallBack ifCreateFragment) {
        showCurrentFragment(fragmentActivity.getSupportFragmentManager(), tag, isAddStack, ifCreateFragment);
    }


    public synchronized static void showCurrentFragment(FragmentManager fragmentManager, String tag, boolean isAddStack, FragmentCallBack ifCreateFragment) {
        try {
            Fragment fragment = FragmentUtils.findFragment(fragmentManager, tag);
            if (fragment == null) {
                addFragment(fragmentManager, tag, isAddStack, ifCreateFragment);
            }
            fragment = FragmentUtils.findFragment(fragmentManager, tag);
            if (fragment != null) {
                FragmentUtils.showHide(fragment, FragmentUtils.getFragments(fragmentManager));
                if (ifCreateFragment != null) {
                    ifCreateFragment.selectFragment(fragment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private synchronized static void addFragment(FragmentManager fragmentManager, String tag, boolean isAddStack, FragmentCallBack ifCreateFragment) {
        Fragment fragment = FragmentUtils.findFragment(fragmentManager, tag);
        if (fragment == null) {
            if (ifCreateFragment != null) {
                Fragment baseFragment = ifCreateFragment.createFragment(tag);
                if (baseFragment != null) {
                    FragmentUtils.add(fragmentManager, baseFragment, ifCreateFragment.context(), tag, R.anim.alpha_in, R.anim.alpha_out);
                }
            }
        }
        FragmentUtils.hide(fragmentManager);
    }

    public interface FragmentCallBack {
        Fragment createFragment(String tag);

        @IdRes
        Integer context();

        void selectFragment(Fragment baseFragment);
    }
}
