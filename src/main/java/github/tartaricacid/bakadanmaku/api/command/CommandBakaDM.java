package github.tartaricacid.bakadanmaku.api.command;

import github.tartaricacid.bakadanmaku.api.thread.BaseDanmakuThread;
import github.tartaricacid.bakadanmaku.api.thread.DanmakuThreadFactory;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandBakaDM extends CommandBase {
    public static final String commandBakaDanmaku = "bakadm"; // 指令头

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
        return 1; // 让任意玩家可以执行此指令
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // 参数为空
        if (args.length == 0) {
            return;
        }

        // 具体的参数分析
        switch (args[0]) {
            // 重载指令
            case "reload": {
                BaseDanmakuThread.sendChatMessage("§8§l正在重启中……");
                DanmakuThreadFactory.restartThreads();
                break;
            }
            default:
                break;
        }
    }
}
