package com.rewardscards.a1rc;

import com.rewardscards.a1rc.Model.MyLatLng;


import java.util.List;

public interface IOnLoadLocationListener {
    void onLoadLocationSuccess(List<MyLatLng> latLngs);
    void onLoadLocationFailed(String message);


}
