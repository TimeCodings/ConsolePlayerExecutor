package dev.timecoding.consoleplayerexecutor;

import dev.timecoding.consoleplayerexecutor.command.ConsolePlayerExecutorCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConsolePlayerExecutor extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendPlainMessage("§aThe plugin §e" + this + " §aby §eTimeCode §ahas been loaded!");

        final ConsolePlayerExecutorCommand command = new ConsolePlayerExecutorCommand();
        final PluginCommand mainCommand = this.getCommand("executeas");

        mainCommand.setExecutor(command);
        mainCommand.setTabCompleter(command);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendPlainMessage("§aThe plugin §e" + this + " §aby §eTimeCode §chas been unloaded!");
    }
}
