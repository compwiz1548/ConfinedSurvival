package com.compwiz1548.ConfinedSurvival;

import com.compwiz1548.ConfinedSurvival.cmd.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CSCommand implements CommandExecutor {
    // map of all sub-commands with the command name (string) for quick reference
    public Map<String, CSCmd> subCommands = new LinkedHashMap<String, CSCmd>();
    // ref. list of the commands which can have a world name in front of the command itself (ex. /wb _world_ radius 100)
    private Set<String> subCommandsWithWorldNames = new LinkedHashSet<String>();
    private boolean wasWorldQuotation = false;


    // constructor
    public CSCommand() {
        addCmd(new CmdHelp());
        addCmd(new CmdReload());
        addCmd(new CmdReset());
        addCmd(new CmdCheck());
        addCmd(new CmdCommands());
    }

    private void addCmd(CSCmd cmd) {
        subCommands.put(cmd.name, cmd);
        if (cmd.hasWorldNameInput)
            subCommandsWithWorldNames.add(cmd.name);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        Player player = (sender instanceof Player) ? (Player) sender : null;

        // if world name is passed inside quotation marks, handle that, and get List<String> instead of String[]
        List<String> params = concatenateQuotedWorldName(split);

        String worldName = null;
        // is second parameter the command and first parameter a world name? definitely world name if it was in quotation marks
        if (wasWorldQuotation || (params.size() > 1 && !subCommands.containsKey(params.get(0)) && subCommandsWithWorldNames.contains(params.get(1))))
            worldName = params.get(0);

        // no command specified? show command examples / help
        if (params.isEmpty())
            params.add(0, "commands");

        // determined the command name
        String cmdName = (worldName == null) ? params.get(0).toLowerCase() : params.get(1).toLowerCase();

        // remove command name and (if there) world name from front of param array
        params.remove(0);
        if (worldName != null)
            params.remove(0);

        // make sure command is recognized, default to showing command examples / help if not; also check for specified page number
        if (!subCommands.containsKey(cmdName)) {
            int page = (player == null) ? 0 : 1;
            try {
                page = Integer.parseInt(cmdName);
            } catch (NumberFormatException ignored) {
                sender.sendMessage(CSCmd.C_ERR + "Command not recognized. Showing command list.");
            }
            cmdName = "commands";
            params.add(0, Integer.toString(page));
        }

        CSCmd subCommand = subCommands.get(cmdName);

        // check permission
        if (!Config.HasPermission(player, subCommand.permission))
            return true;

        // if command requires world name when run by console, make sure that's in place
        if (player == null && subCommand.hasWorldNameInput && subCommand.consoleRequiresWorldName && worldName == null) {
            sender.sendMessage(CSCmd.C_ERR + "This command requires a world to be specified if run by the console.");
            subCommand.sendCmdHelp(sender);
            return true;
        }

        // make sure valid number of parameters has been provided
        if (params.size() < subCommand.minParams || params.size() > subCommand.maxParams) {
            if (subCommand.maxParams == 0)
                sender.sendMessage(CSCmd.C_ERR + "This command does not accept any parameters.");
            else
                sender.sendMessage(CSCmd.C_ERR + "You have not provided a valid number of parameters.");
            subCommand.sendCmdHelp(sender);
            return true;
        }

        // execute command
        subCommand.execute(sender, player, params, worldName);

        return true;
    }

    // if world name is surrounded by quotation marks, combine it down and flag wasWorldQuotation if it's first param.
    // also return List<String> instead of input primitive String[]
    private List<String> concatenateQuotedWorldName(String[] split) {
        wasWorldQuotation = false;
        List<String> args = new ArrayList<String>(Arrays.asList(split));

        int startIndex = -1;
        for (int i = 0; i < args.size(); i++) {
            if (args.get(i).startsWith("\"")) {
                startIndex = i;
                break;
            }
        }
        if (startIndex == -1)
            return args;

        if (args.get(startIndex).endsWith("\"")) {
            args.set(startIndex, args.get(startIndex).substring(1, args.get(startIndex).length() - 1));
            if (startIndex == 0)
                wasWorldQuotation = true;
        } else {
            List<String> concat = new ArrayList<String>(args);
            Iterator<String> concatI = concat.iterator();

            // skip past any parameters in front of the one we're starting on
            for (int i = 1; i < startIndex + 1; i++) {
                concatI.next();
            }

            StringBuilder quote = new StringBuilder(concatI.next());
            while (concatI.hasNext()) {
                String next = concatI.next();
                concatI.remove();
                quote.append(" ");
                quote.append(next);
                if (next.endsWith("\"")) {
                    concat.set(startIndex, quote.substring(1, quote.length() - 1));
                    args = concat;
                    if (startIndex == 0)
                        wasWorldQuotation = true;
                    break;
                }
            }
        }
        return args;
    }

    public Set<String> getCommandNames() {
        // using TreeSet to sort alphabetically
        Set<String> commands = new TreeSet(subCommands.keySet());
        // removing default "commands" command as it's not normally shown or run like other commands
        commands.remove("commands");
        return commands;
    }

}