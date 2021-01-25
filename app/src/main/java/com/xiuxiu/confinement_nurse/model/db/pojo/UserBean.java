package com.xiuxiu.confinement_nurse.model.db.pojo;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.xiuxiu.confinement_nurse.model.db.dao.converters.AvatarListListConverters;

import java.io.Serializable;

import static com.xiuxiu.confinement_nurse.Constants.KEY_AGENCY;
import static com.xiuxiu.confinement_nurse.Constants.KEY_USE;

/**
 * 必须要用@Entity来注解这个类
 * 开头定义的变量里面至少有一个主键注解@PrimaryKey,并且为true
 *
 * @Ignore 不想持久的字段
 * @PrimaryKey 主键
 * Room使用字段名称作为数据库中的列名。如果希望列具有不同的名称，请将@ColumnInfo注解添加到字段中
 * Room使用类名作为数据库表名。如果希望表具有不同的名称，请设置@Entity注解的tableName属性
 * 加快查询 ，可以添加索引
 * 可以使用@ForeignKey 注解定义其与用户实体的关系，
 */

/**
 * 登录的用户
 */
@Entity(tableName = "user", indices = {@Index("id")})
@TypeConverters(AvatarListListConverters.class)
public class UserBean implements Serializable {

    @PrimaryKey
    @NonNull
    @SerializedName(value = "user_id", alternate = "uid")
    private String id;

    private String name;
    private String token;

    @Embedded
    private UserInfoBean item;
    @Embedded
    private AgencyInfoBean agencyInfoBean;

    private String userType=KEY_USE;

    public AgencyInfoBean getAgencyInfoBean() {
        return agencyInfoBean;
    }

    public void setAgencyInfoBean(AgencyInfoBean agencyInfoBean) {
        this.agencyInfoBean = agencyInfoBean;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Embedded
    @Ignore
    private CertificateInfoBean certificateInfoBean;

    public CertificateInfoBean getCertificateInfoBean() {
        return certificateInfoBean;
    }

    public void setCertificateInfoBean(CertificateInfoBean certificateInfoBean) {
        this.certificateInfoBean = certificateInfoBean;
    }

    public UserInfoBean getItem() {
        return item == null ? new UserInfoBean() : item;
    }

    public void setItem(UserInfoBean item) {
        this.item = item;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserBean(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", item=" + item +
                ", agencyInfoBean=" + agencyInfoBean +
                ", userType='" + userType + '\'' +
                ", certificateInfoBean=" + certificateInfoBean +
                '}';
    }
}
