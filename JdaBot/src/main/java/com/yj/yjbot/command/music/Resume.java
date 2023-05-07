package com.yj.yjbot.command.music;

import com.yj.yjbot.command.Command;
import com.yj.yjbot.lavaplayer.GuildMusicManager;
import com.yj.yjbot.lavaplayer.PlayerManager;
import com.yj.yjbot.lavaplayer.TrackScheduler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Resume implements Command {
    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public String getDescription() {
        return "현재 정지된 트랙을 재실행합니다.";
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
            trackScheduler.getPlayer().setPaused(false);

            event.reply("Resume").setEphemeral(true).queue();
        }
    }
}
