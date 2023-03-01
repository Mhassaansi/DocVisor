package com.modules.facebooklogin;

/**
 * Created by Hamza and Muzz on 6/3/2019.
 */
public interface FacebookResponse {
    void onFbSignInFail();

    void onFbSignInSuccess();

    void onFbProfileReceived(FacebookUser facebookUser);

    void onFBSignOut();
}
