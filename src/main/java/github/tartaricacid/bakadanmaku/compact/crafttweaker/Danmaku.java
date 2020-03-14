package github.tartaricacid.bakadanmaku.compact.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import github.tartaricacid.bakadanmaku.compact.crafttweaker.action.GiftAction;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.baka_danmaku.danmaku")
public class Danmaku {

    //注册新的礼物命令
    @ZenMethod
    public static void registerGiftAction(String giftName, int num, String command) {
        CraftTweakerAPI.apply(new GiftAction(giftName, num, command));
    }
}
