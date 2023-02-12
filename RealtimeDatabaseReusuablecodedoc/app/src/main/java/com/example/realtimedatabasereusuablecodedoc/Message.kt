package com.example.realtimedatabasereusuablecodedoc

//this class represents each individual message displayed in
//the chat. contains 2 members: the message itself and the sender
//uid.
class Message {
    var message: String? = null
    var senderUid: String? = null

    constructor(){}

    constructor(message: String?, uid: String?) {
        this.message = message
        this.senderUid = uid
    }
}