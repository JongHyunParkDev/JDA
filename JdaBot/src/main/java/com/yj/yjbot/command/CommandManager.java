package com.yj.yjbot.command;

import com.yj.yjbot.schedule.TaskSchedule;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;

public class CommandManager extends ListenerAdapter {
    private Map<String, Command> commandMap = new HashMap<>();

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        TaskSchedule.textChannel = event.getGuild().getDefaultChannel().asTextChannel();
    }

    @Override
    public void onReady(ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            for (Command command : commandMap.values()) {
                if (command.getOptions() == null) {
                    guild.upsertCommand(command.getName(),
                            command.getDescription())
                            .queue();
                } else {
                    guild.upsertCommand(command.getName(), command.getDescription()).addOptions(command.getOptions()).queue();
                }
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        commandMap.get(event.getName()).execute(event);
    }

    public void add(Command command) {
        commandMap.put(command.getName(), command);
    }
}
