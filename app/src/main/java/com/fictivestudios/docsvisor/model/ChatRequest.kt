package com.fictivestudios.docsvisor.model

data class ChatRequest(
    var sender_id:String, var reciever_id:String)
{

    constructor( sender_id:String,  reciever_id:String,  message:String,type:String) :
            this(sender_id,reciever_id) {

    }

}
