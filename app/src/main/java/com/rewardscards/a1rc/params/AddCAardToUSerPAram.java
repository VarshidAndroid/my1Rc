package com.rewardscards.a1rc.params;

public class AddCAardToUSerPAram {
    String user_id;
    String card_number;
    String card_id;

    public AddCAardToUSerPAram(String user_id, String card_number, String card_id) {
        this.user_id = user_id;
        this.card_number = card_number;
        this.card_id = card_id;
    }
}
