package com.github.tartaricacid.bakadanmaku.event.post;


import cpw.mods.fml.common.eventhandler.Event;

public class SendDanmakuEvent extends Event {
    private final String message;

    public SendDanmakuEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
