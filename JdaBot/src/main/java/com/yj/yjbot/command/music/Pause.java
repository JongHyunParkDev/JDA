package com.yj.yjbot.command.music;

import com.yj.yjbot.command.Command;
import com.yj.yjbot.lavaplayer.GuildMusicManager;
import com.yj.yjbot.lavaplayer.PlayerManager;
import com.yj.yjbot.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Pause implements Command {
    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getDescription() {
        return "현재 재생중인 트랙을 정지합니다.";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (isInGuildMusicCommand(event)) {
            GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
            TrackScheduler trackScheduler = guildMusicManager.getTrackScheduler();
            trackScheduler.getPlayer().setPaused(true);

            event.reply("pause").setEphemeral(true).queue();
        }
    }
}
