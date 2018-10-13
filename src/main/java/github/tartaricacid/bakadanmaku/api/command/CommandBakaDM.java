package github.tartaricacid.bakadanmaku.api.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

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

    }
}
