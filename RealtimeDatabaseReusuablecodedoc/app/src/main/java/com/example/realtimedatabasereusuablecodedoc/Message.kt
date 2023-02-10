package com.example.realtimedatabasereusuablecodedoc

class Message {
    var message: String? = null
    var senderEmail: String? = null

    constructor(){}

    constructor(message: String?, email: String?) {
        this.message = message
        this.senderEmail = email
    }
}