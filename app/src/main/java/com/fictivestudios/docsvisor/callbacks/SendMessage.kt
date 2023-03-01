package com.fictivestudios.docsvisor.callbacks

import org.json.JSONObject

interface SendMessage {

    fun onMessageSend(jsonObject: JSONObject)
}