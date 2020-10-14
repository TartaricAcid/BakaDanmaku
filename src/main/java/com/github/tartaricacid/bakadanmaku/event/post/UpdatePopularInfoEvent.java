package com.github.tartaricacid.bakadanmaku.event.post;

import net.minecraftforge.fml.common.eventhandler.Event;

public class UpdatePopularInfoEvent extends Event {
    private final int popular;

    public UpdatePopularInfoEvent(int popular) {
        this.popular = popular;
    }

    public int getPopular() {
        return popular;
    }
}
