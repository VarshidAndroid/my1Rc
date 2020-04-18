package com.rewardscards.a1rc.params;

public class StoreListModel {
    String store_group_id;
    String store_category_id;

    public StoreListModel(String store_group_id, String store_category_id) {
        this.store_group_id = store_group_id;
        this.store_category_id = store_category_id;
    }

    public String getStore_group_id() {
        return store_group_id;
    }

    public void setStore_group_id(String store_group_id) {
        this.store_group_id = store_group_id;
    }

    public String getStore_category_id() {
        return store_category_id;
    }

    public void setStore_category_id(String store_category_id) {
        this.store_category_id = store_category_id;
    }
}
