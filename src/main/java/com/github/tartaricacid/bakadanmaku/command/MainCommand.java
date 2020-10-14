package com.github.tartaricacid.bakadanmaku.command;

import com.github.tartaricacid.bakadanmaku.utils.OpenCloseDanmaku;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;
import java.util.List;

public class MainCommand extends CommandBase {
    private static final String NAME = "BakaDanmaku";
    private static final String USAGE = "/BakaDanmaku";

    @Override
    public String getCommandName() {
        return NAME;
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("bd");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return USAGE;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayerSP) {
            OpenCloseDanmaku.closeDanmaku();
            sender.addChatMessage(new ChatComponentText("直播间正在重载中……"));
            OpenCloseDanmaku.openDanmaku();
        }
    }
}
