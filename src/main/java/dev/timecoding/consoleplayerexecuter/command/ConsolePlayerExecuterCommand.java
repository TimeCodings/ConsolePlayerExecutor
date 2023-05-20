package dev.timecoding.consoleplayerexecuter.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConsolePlayerExecuterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("execute.as.player")) {
            if (args.length >= 2) {
                String playerName = args[0];
                Player target = Bukkit.getPlayer(playerName);
                if (playerName.equalsIgnoreCase("@a") || target != null && target.isOnline()) {
                    String executableCommand = args[1];
                    for (int i = 2; i < args.length; i++) {
                        executableCommand = executableCommand + " " + args[i];
                    }
                    if (executableCommand.endsWith(" ")) {
                        executableCommand = executableCommand.substring(0, (executableCommand.length() - 1));
                    }
                    if (executableCommand.startsWith("/")) {
                        executableCommand.substring(1, executableCommand.length());
                    }
                    if(playerName.equalsIgnoreCase("@a")){
                        String finalExecutableCommand = executableCommand;
                        Bukkit.getOnlinePlayers().forEach(player -> player.performCommand(finalExecutableCommand));
                        sender.sendMessage("§aThe command §e" + executableCommand + " §awas executed successfully as §eall players!");
                    }else {
                        target.performCommand(executableCommand);
                        sender.sendMessage("§aThe command §e" + executableCommand + " §awas executed successfully as the player §e" + target.getName() + "!");
                    }
                } else {
                    sender.sendMessage("§cThis player isn't online right now!");
                }
            } else {
                sender.sendMessage("§cPlease use the syntax §e/executeasplayer <PlayerName> <PlayerCommand>");
            }
        } else {
            sender.sendMessage("§cHey sorry, but you need the permission §eexecute.as.player §cto use this command!");
        }
        return false;
    }
}
