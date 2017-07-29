package org.maxgamer.maxbans.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.maxgamer.maxbans.exception.MessageException;
import org.maxgamer.maxbans.locale.Locale;
import org.maxgamer.maxbans.locale.MessageBuilder;
import org.maxgamer.maxbans.service.BroadcastService;
import org.maxgamer.maxbans.service.LocatorService;
import org.maxgamer.maxbans.service.MetricService;
import org.maxgamer.maxbans.transaction.Transactor;
import org.maxgamer.maxbans.util.RestrictionUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * @author netherfoam
 */
public class KickCommand extends StandardCommandExecutor {
    private LocatorService locatorService;
    private BroadcastService broadcastService;
    private MetricService metricService;

    public KickCommand(Transactor transactor, Locale locale, Logger logger, LocatorService locatorService, BroadcastService broadcastService, MetricService metrics) {
        super(transactor, locale, logger, "maxbans.kick");

        this.locatorService = locatorService;
        this.broadcastService = broadcastService;
        this.metricService = metrics;
    }

    @Override
    public void perform(CommandSender sender, Command command, String s, String[] userArgs) throws MessageException {
        LinkedList<String> args = new LinkedList<>(Arrays.asList(userArgs));
        boolean silent = RestrictionUtil.isSilent(args);

        if(args.size() <= 0) {
            sender.sendMessage("Must supply target name");
            return;
        }

        Player player = locatorService.player(args.pop());
        if(player == null) {
            sender.sendMessage("Player not found");
            return;
        }

        String reason = String.join(" ", args);
        MessageBuilder properties = locale.get()
                .with("source", sender instanceof Player ? sender.getName() : "Console")
                .with("name", player.getName())
                .with("reason", reason);

        player.kickPlayer(properties.get("kick.message"));

        broadcastService.broadcast(properties.get("kick.broadcast"), silent, sender);
        metricService.increment(MetricService.KICKS);
    }
}
