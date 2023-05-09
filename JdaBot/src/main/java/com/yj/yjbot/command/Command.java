package com.yj.yjbot.command;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public interface Command {
    String getName();

    String getDescription();

    List<OptionData> getOptions();

    void execute(SlashCommandInteractionEvent event);

    default boolean isInGuildMusicCommand(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState guildVoiceState = member.getVoiceState();

        if (! guildVoiceState.inAudioChannel()) {
            event.reply("You are not in Voice Channel").setEphemeral(true).queue();
            return false;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if (! selfVoiceState.inAudioChannel()) {
            event.reply("I (Bot) am not in an audio channel").setEphemeral(true).queue();
            return false;
        }

        if (selfVoiceState.getChannel() != guildVoiceState.getChannel()) {
            event.reply("You are not in same channel as Bot").setEphemeral(true).queue();
            return false;
        }
        return true;
    }
}
