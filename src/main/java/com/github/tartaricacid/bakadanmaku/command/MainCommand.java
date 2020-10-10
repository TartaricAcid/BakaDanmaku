package com.github.tartaricacid.bakadanmaku.command;

import com.github.tartaricacid.bakadanmaku.config.ConfigManger;
import com.github.tartaricacid.bakadanmaku.gui.bilibili.RoomGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

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
            RoomGui roomGui = new RoomGui(ConfigManger.getBilibiliConfig());
            Runnable displayGui = () -> Minecraft.getMinecraft().displayGuiScreen(roomGui);
            Runnable addScheduledTask = () -> Minecraft.getMinecraft().addScheduledTask(displayGui);
            Thread thread = new Thread(addScheduledTask);
            thread.start();
        }
    }
}
