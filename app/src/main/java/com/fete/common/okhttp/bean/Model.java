package com.fete.common.okhttp.bean;

import java.io.Serializable;

public class Model {
    public Model(int type, BinaryBean binary, RefreshBean refresh) {
        this.type = type;
        this.binary = binary;
        this.refresh = refresh;
    }

    public Model(int type, BinaryBean binary) {
        this.type = type;
        this.binary = binary;
    }

    public Model(int type, RefreshBean refresh) {
        this.type = type;
        this.refresh = refresh;
    }

    public Model(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public BinaryBean getBinary() {
        return binary;
    }

    public void setBinary(BinaryBean binary) {
        this.binary = binary;
    }

    public RefreshBean getRefresh() {
        return refresh;
    }

    public void setRefresh(RefreshBean refresh) {
        this.refresh = refresh;
    }

    int type;
    private BinaryBean binary;
    private RefreshBean refresh;

    public static class BinaryBean implements Serializable {
        String name;
        String nickname;
        String imageId;
        int avatarId;

        public BinaryBean(String name, String nickname, String imageId) {
            this.name = name;
            this.nickname = nickname;
            this.imageId = imageId;
            this.avatarId = avatarId;
        }

        public BinaryBean() {

        }

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public int getAvatarId() {
            return avatarId;
        }

        public void setAvatarId(int avatarId) {
            this.avatarId = avatarId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        @Override
        public String toString() {
            return "BinaryBean{" +
                    "imageId='" + imageId + '\'' +
                    ", avatarId=" + avatarId +
                    ", name='" + name + '\'' +
                    ", nickname='" + nickname + '\'' +
                    '}';
        }
    }

    public static class RefreshBean implements Serializable {
        String imageId;
        int avatarId;

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public int getAvatarId() {
            return avatarId;
        }

        public void setAvatarId(int avatarId) {
            this.avatarId = avatarId;
        }

        @Override
        public String toString() {
            return "RefreshBean{" +
                    "imageId='" + imageId + '\'' +
                    ", avatarId=" + avatarId +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Model{" +
                "type=" + type +
                ", binary=" + binary +
                ", refresh=" + refresh +
                '}';
    }
}