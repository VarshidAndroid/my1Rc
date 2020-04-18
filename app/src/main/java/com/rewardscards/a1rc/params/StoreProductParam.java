package com.rewardscards.a1rc.params;

public class StoreProductParam {
    String store_group_id;
    String store_category_id;
    String store_number;

    public StoreProductParam(String store_group_id, String store_category_id, String store_number) {
        this.store_group_id = store_group_id;
        this.store_category_id = store_category_id;
        this.store_number = store_number;
    }
}
