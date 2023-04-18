package com.yj.yjbot.command;

import ca.tristan.easycommands.commands.CommandExecutor;
import ca.tristan.easycommands.commands.EventData;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class SayCmd extends CommandExecutor {
    @Override
    public String getName() {
        return "say";
    }

    @Override
    public String getDescription() {
        return "/say <message> - The bot will send you back the message.";
    }

    @Override
    public boolean isOwnerOnly() {
        return false;
    }

    @Override
    public List<OptionData> getOptions() {
        options.add(new OptionData(OptionType.STRING, "message", "This message will get sent by the bot."));
        return options;
    }

    @Override
    public void execute(EventData data) {
        if (data.getCommand().getOptions().isEmpty()) {
            data.getEvent().reply("You need to specify a message.").setEphemeral(true).queue();
            return;
        }
        data.deferReply().queue();
        data.getHook().sendMessage(data.getCommand().getOptions().get(0).getAsString()).queue();
    }
}
