package github.tartaricacid.bakadanmaku.compact.crafttweaker.action;

import crafttweaker.IAction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GiftAction implements IAction {

    //用于存储所有礼物命令
    public static HashMap<String, List<GiftAction>> giftActions = new HashMap<>();

    public String giftName, command;
    public int num;

    public GiftAction(String giftName, int num, String command) {
        this.giftName = giftName;
        this.num = num;
        this.command = command;
    }

    @Override
    public void apply() {
        //如果该礼物没有任何命令 则新建一个列表
        if (!giftActions.containsKey(this.giftName)) {
            List<GiftAction> list = new LinkedList<>();
            list.add(this);
            giftActions.put(this.giftName, list);
        }
        else {
            giftActions.get(this.giftName).add(this);
        }
    }

    @Override
    public String describe() {
        return String.format("超过数量[%s]的礼物[%s]会执行命令[%s]", this.num, this.giftName, this.command);
    }
}
