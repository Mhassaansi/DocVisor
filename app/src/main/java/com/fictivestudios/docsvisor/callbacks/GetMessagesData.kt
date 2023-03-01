package com.fictivestudios.docsvisor.callbacks

import org.json.JSONObject

interface GetMessagesData {

    fun getMessageData(jsonObject: JSONObject)
}