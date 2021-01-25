package com.xiuxiu.confinement_nurse.ui.my.vm;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TeamVm implements Serializable {

    @SerializedName("item")
    private Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public static class Team implements Serializable {

        /**
         * matronId : -7.64433796218302E7
         * groupName : quis
         * code : consequat Duis proident
         */

        private String matronId;
        private String groupName;
        private String code;

        public String getMatronId() {
            return matronId;
        }

        public void setMatronId(String matronId) {
            this.matronId = matronId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
