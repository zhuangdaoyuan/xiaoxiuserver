package com.xiuxiu.confinement_nurse.ui.my;


import com.xiuxiu.confinement_nurse.ui.base.mvp.Viewer;
import com.xiuxiu.confinement_nurse.ui.my.vm.TeamVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.UserStateInfoCodeVm;
import com.xiuxiu.confinement_nurse.ui.my.vm.UserStateInfoVm;

public interface CenterContract {
    interface IView extends Viewer {
        void onResquestUserInfo(UserStateInfoVm.UserStateInfo item);

        void onResquestUserInfoCode(UserStateInfoCodeVm s);

        void onRequestUserTeam(TeamVm s);
    }

    interface IPresenter {
        public void requestData();

        public void requestTeamInformation(String code);

        public void requestInvitationCode();

        public void requestJoinTheTeam(String code);
    }
}
