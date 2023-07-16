package dev.timecoding.consoleplayerexecuter.command.completer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConsolePlayerExecuterCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> returnList = new ArrayList<>();
        if(command.getName().equalsIgnoreCase("executeas") && command.getAliases().contains("executeasplayer")){
            if(args.length == 1){
                if(args[0].endsWith(",")){
                    for (Player allWhichAreOnline : Bukkit.getOnlinePlayers()) {
                        returnList.add(args[0]+allWhichAreOnline.getName());
                    }
                }else if(!args[0].contains(",")){
                    returnList.add("@a");
                    for (Player allWhichAreOnline : Bukkit.getOnlinePlayers()) {
                        returnList.add(allWhichAreOnline.getName());
                        returnList.add(allWhichAreOnline.getName()+",");
                    }
                }
            }else if(args.length == 2){
                returnList.add("YOURCOMMAND");
            }
        }
        return returnList;
    }
}
