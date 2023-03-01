package com.modules.facebooklogin;

import org.json.JSONObject;

/**
 * Created by Hamza and Muzz on 6/3/2019.
 * This class represents facebook user profile.
 */
public class FacebookUser {
    public String name;

    public String email;

    public String facebookID;

    public String gender;

    public String about;

    public String bio;

    public String coverPicUrl;

    public String profilePic;

    /**
     * JSON response received. If you want to parse more fields.
     */
    public JSONObject response;

}
