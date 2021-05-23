package com.example.sample.usertribe.Common;

import com.example.sample.usertribe.Interface.FCMClient;
import com.example.sample.usertribe.Interface.IFCMService;

/**
 * Created by Hassan Javaid on 8/17/2018.
 */

public class Common {
    public static final String fcmURL="https://fcm.googleapis.com/";
    public static IFCMService getFCMService()
    {
        return FCMClient.getClient(fcmURL).create(IFCMService.class);
    }
}
