package dev.timecoding.consoleplayerexecutor.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ConsolePlayerExecutorCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("execute.as.player") || sender.hasPermission("execute.as")) {
            if (args.length >= 2) {
                String playerName = args[0];
                Player target = Bukkit.getPlayer(playerName);
                if (playerName.equalsIgnoreCase("@a") || playerName.contains(",") || target != null && target.isOnline()) {
                    StringBuilder executableCommand = new StringBuilder(args[1]);
                    for (int i = 2; i < args.length; i++) {
                        executableCommand.append(" ").append(args[i]);
                    }
                    if (executableCommand.toString().endsWith(" ")) {
                        executableCommand = new StringBuilder(executableCommand.substring(0, (executableCommand.length() - 1)));
                    }
                    if (playerName.equalsIgnoreCase("@a")) {
                        String finalExecutableCommand = executableCommand.toString();
                        Bukkit.getOnlinePlayers().forEach(player -> player.performCommand(finalExecutableCommand));
                        sender.sendPlainMessage("§aThe command §e" + executableCommand + " §awas executed successfully as §eall players!");
                    } else if (playerName.contains(",")) {
                        String[] split = playerName.split(",");
                        List<String> players = new ArrayList<>();
                        List<String> notOnline = new ArrayList<>();
                        Arrays.stream(split).forEach(s -> {
                            Player player = Bukkit.getPlayer(s);
                            if (player != null && player.isOnline()) {
                                if (!players.contains(player.getName())) {
                                    players.add(player.getName());
                                }
                            } else {
                                notOnline.add(s);
                            }
                        });
                        if (players.size() > 0) {
                            sender.sendPlainMessage("§aThe command §e" + executableCommand + " §awas executed successfully as §e" + this.getPlayerStringList(players));
                            String finalExecutableCommand = executableCommand.toString();
                            players.forEach(s -> Objects.requireNonNull(Bukkit.getPlayer(s)).performCommand(finalExecutableCommand));
                            if (notOnline.size() > 0) {
                                sender.sendPlainMessage("§cUnfortunately, the command could not be executed as §e" + this.getPlayerStringList(notOnline));
                            }
                        } else {
                            sender.sendPlainMessage("§cThe following players aren't online: §e" + this.getPlayerStringList(notOnline));
                        }
                    } else {
                        assert target != null;
                        target.performCommand(executableCommand.toString());
                        sender.sendPlainMessage("§aThe command §e" + executableCommand + " §awas executed successfully as the player §e" + target.getName() + "!");
                    }
                } else {
                    sender.sendPlainMessage("§cThe player §e" + playerName + " §cis not online right now!");
                }
            } else {
                sender.sendPlainMessage("§cPlease use the syntax §e/executeasplayer <PlayerName> <PlayerCommand>");
            }
        } else {
            sender.sendPlainMessage("§cHey sorry, but you need the permission §eexecute.as.player §cto use this command!");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> returnList = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("executeas") && command.getAliases().contains("executeasplayer")) {
            if (args.length == 1) {
                if (args[0].endsWith(",")) {
                    for (Player allWhichAreOnline : Bukkit.getOnlinePlayers()) {
                        returnList.add(args[0] + allWhichAreOnline.getName());
                    }
                } else if (!args[0].contains(",")) {
                    returnList.add("@a");
                    for (Player allWhichAreOnline : Bukkit.getOnlinePlayers()) {
                        returnList.add(allWhichAreOnline.getName());
                        returnList.add(allWhichAreOnline.getName() + ",");
                    }
                }
            } else if (args.length == 2) {
                returnList.add("YOURCOMMAND");
            }
        }
        return returnList;
    }

    private String getPlayerStringList(final List<String> strings) {
        AtomicReference<String> string = new AtomicReference<>("");
        strings.forEach(s -> {
            string.set(string + s + ", ");
        });
        string.set(string.get().substring(0, (string.get().length() - 2)));
        return string.get();
    }
}
