package com.rewardscards.a1rc.Model;

public class MenuModel {
    public String menuName, code;
    public boolean hasChildren, isGroup;

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, String code) {

        this.menuName = menuName;
        this.code = code;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }
}
