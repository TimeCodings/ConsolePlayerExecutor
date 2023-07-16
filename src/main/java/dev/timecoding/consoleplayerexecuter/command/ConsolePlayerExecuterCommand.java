package dev.timecoding.consoleplayerexecuter.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ConsolePlayerExecuterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("execute.as.player") || sender.hasPermission("execute.as")) {
            if (args.length >= 2) {
                String playerName = args[0];
                Player target = Bukkit.getPlayer(playerName);
                if (playerName.equalsIgnoreCase("@a") || playerName.contains(",") || target != null && target.isOnline()) {
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
                    }else if(playerName.contains(",")){
                        String[] split = playerName.split(",");
                        List<String> players = new ArrayList<>();
                        List<String> notOnline = new ArrayList<>();
                        Arrays.stream(split).forEach(s -> {
                            Player player = Bukkit.getPlayer(s);
                            if(player != null && player.isOnline()){
                                if(!players.contains(player.getName())) {
                                    players.add(player.getName());
                                }
                            }else{
                                notOnline.add(s);
                            }
                        });
                        if(players.size() > 0){
                            sender.sendMessage("§aThe command §e"+executableCommand+" §awas executed successfully as §e"+this.getPlayerStringList(players));
                            if(notOnline.size() > 0){
                                sender.sendMessage("§cUnfortunately, the command could not be executed as §e"+this.getPlayerStringList(notOnline));
                            }
                        }else{
                            sender.sendMessage("§cThe following players aren't online: §e"+this.getPlayerStringList(notOnline));
                        }
                    }else {
                        target.performCommand(executableCommand);
                        sender.sendMessage("§aThe command §e" + executableCommand + " §awas executed successfully as the player §e" + target.getName() + "!");
                    }
                } else {
                    sender.sendMessage("§cThe player §e"+playerName+" §cisn't online right now!");
                }
            } else {
                sender.sendMessage("§cPlease use the syntax §e/executeasplayer <PlayerName> <PlayerCommand>");
            }
        } else {
            sender.sendMessage("§cHey sorry, but you need the permission §eexecute.as.player §cto use this command!");
        }
        return false;
    }

    private String getPlayerStringList(List<String> strings){
        AtomicReference<String> string = new AtomicReference<>("");
        strings.forEach(s -> {
            string.set(string + s + ", ");
        });
        string.set(string.get().substring(0, (string.get().length()-2)));
        return string.get();
    }
}
