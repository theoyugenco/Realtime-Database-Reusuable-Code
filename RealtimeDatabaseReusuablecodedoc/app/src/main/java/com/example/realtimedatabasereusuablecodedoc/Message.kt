package com.example.realtimedatabasereusuablecodedoc

class Message {
    var message: String? = null
    var senderUid: String? = null

    constructor(){}

    constructor(message: String?, uid: String?) {
        this.message = message
        this.senderUid = uid
    }
}