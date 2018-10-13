package github.tartaricacid.bakadanmaku.api.command;

import github.tartaricacid.bakadanmaku.api.thread.DanmakuThreadFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandBakaDM extends CommandBase {
    public static final String commandBakaDanmaku = "bakadm";

    @Override
    public String getName() {
        return commandBakaDanmaku;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.bakadm.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        // Any player can execute this command.
        return 1;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            return;
        }
        switch (args[0]) {
            case "reload": {
                if (Minecraft.getMinecraft().player != null)
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString("§8§l正在重启中……"));

                DanmakuThreadFactory.restartCurrentThread();
                break;
            }
            default:
                break;
        }
    }
}
