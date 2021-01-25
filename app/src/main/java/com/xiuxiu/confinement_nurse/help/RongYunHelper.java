package com.xiuxiu.confinement_nurse.help;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.xiuxiu.confinement_nurse.BuildConfig;
import com.xiuxiu.confinement_nurse.R;
import com.xiuxiu.confinement_nurse.model.ModelManager;
import com.xiuxiu.confinement_nurse.utlis.xfunc.XFunc1;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.ipc.RongService;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.Conversation;

import static io.rong.imlib.RongIMClient.DatabaseOpenStatus.DATABASE_OPEN_SUCCESS;

public final class RongYunHelper {

    public static void init(Application a) {
        //        String appKey = "融云开发者后台创建的应用的 AppKey"
//        RongIM.init(a, "25wehl3u203zw", false);
        RongIM.init(a, "qd46yzrfqxrbf", false);
    }

    /**
     * 聊天
     */
    public static void customerService(String token, XFunc1<Integer> callback) {
        Log.i("customerService",token);
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            @Override
            public void onSuccess(String s) {

                callback.call(0);
            }

            @Override
            public void onError(RongIMClient.ConnectionErrorCode connectionErrorCode) {
                Log.i("customerService:onError",connectionErrorCode.toString());
                callback.call(-1);
            }

            @Override
            public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus databaseOpenStatus) {
                Log.i("customerService:","onDatabaseOpened:"+databaseOpenStatus.toString());
                callback.call(-1);
            }
        });
    }

    /**
     * 会话列表
     */
    public static void startConversationList(Context context) {
        Map<String, Boolean> map = new HashMap<>();
        map.put(Conversation.ConversationType.PRIVATE.getName(), true); // 会话列表需要显示私聊会话, 第二个参数 true 代表私聊会话需要聚合显示
        map.put(Conversation.ConversationType.GROUP.getName(), false);  // 会话列表需要显示群组会话, 第二个参数 false 代表群组会话不需要聚合显示
        RongIM.getInstance().startConversationList(context, map);
    }

    /**
     * 加载 会话列表 ConversationListFragment
     */
    public static void enterFragment(Fragment fragmentActivity) {
        ConversationListFragment fragment = (ConversationListFragment) fragmentActivity.getChildFragmentManager().findFragmentById(R.id.conversationlist);

        // 此处设置 Uri. 通过 appendQueryParameter 去设置所要支持的会话类型. 例如
        // .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(),"false")
        // 表示支持单聊会话, false 表示不聚合显示, true 则为聚合显示
        Uri uri = Uri.parse("rong://" + fragmentActivity.getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                .build();


        fragment.setUri(uri);

    }

    public static void kefu(Context view, String token) {
        //首先需要构造使用客服者的用户信息
        CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
        CSCustomServiceInfo csInfo = csBuilder.nickName("融云").build();

/**
 * 启动客户服聊天界面。
 *
 * @param context           应用上下文。
 * @param customerServiceId 要与之聊天的客服 Id。
 * @param title             聊天的标题，开发者可以在聊天界面通过 intent.getData().getQueryParameter("title") 获取该值, 再手动设置为标题。
 * @param customServiceInfo 当前使用客服者的用户信息。{@link io.rong.imlib.model.CSCustomServiceInfo}
 */
        RongIM.getInstance().startCustomerServiceChat(view, token, "在线客服", csInfo);
    }
}
