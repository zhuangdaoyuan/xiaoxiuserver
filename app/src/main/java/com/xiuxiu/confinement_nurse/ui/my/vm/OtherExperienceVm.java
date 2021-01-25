package com.xiuxiu.confinement_nurse.ui.my.vm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OtherExperienceVm implements Serializable {
    private List<OtherExperience> items;


    public List<OtherExperience> getItems() {
        return items==null?new ArrayList<>():items;
    }

    public void setItems(List<OtherExperience> items) {
        this.items = items;
    }

    public static class OtherExperience implements Serializable{

        /**
         * id : 8.867319893697676E7
         * serviceType : -3.6873022517113395E7
         * objectType : -6709531.1280376315
         * groupType : -1.741835500360182E7
         * groupName : ut
         * location : id Lorem in occaecat
         * serviceStartTime : do
         * serviceEndTime : sunt aliqua
         * images : enim
         */

        private String id;
        private String serviceType;
        private String objectType;
        private String groupType;
        private String groupName;
        private String location;
        private String serviceStartTime;
        private String serviceEndTime;
        private String images;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }

        public String getObjectType() {
            return objectType;
        }

        public void setObjectType(String objectType) {
            this.objectType = objectType;
        }

        public String getGroupType() {
            return groupType;
        }

        public void setGroupType(String groupType) {
            this.groupType = groupType;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getServiceStartTime() {
            return serviceStartTime;
        }

        public void setServiceStartTime(String serviceStartTime) {
            this.serviceStartTime = serviceStartTime;
        }

        public String getServiceEndTime() {
            return serviceEndTime;
        }

        public void setServiceEndTime(String serviceEndTime) {
            this.serviceEndTime = serviceEndTime;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }
    }
}
