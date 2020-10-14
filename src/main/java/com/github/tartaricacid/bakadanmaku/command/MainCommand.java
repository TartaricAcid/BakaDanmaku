package com.github.tartaricacid.bakadanmaku.command;

import com.github.tartaricacid.bakadanmaku.utils.OpenCloseDanmaku;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.Collections;
import java.util.List;

public class MainCommand extends CommandBase {
    private static final String NAME = "BakaDanmaku";
    private static final String USAGE = "/BakaDanmaku";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("bd");
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return USAGE;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayerSP) {
            OpenCloseDanmaku.closeDanmaku();
            sender.sendMessage(new TextComponentString("直播间正在重载中……"));
            OpenCloseDanmaku.openDanmaku();
        }
    }
}
