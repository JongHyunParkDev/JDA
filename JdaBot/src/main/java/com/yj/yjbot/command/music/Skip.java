package com.yj.yjbot.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yj.yjbot.command.Command;
import com.yj.yjbot.lavaplayer.GuildMusicManager;
import com.yj.yjbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Skip implements Command {
    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "현재 재생중인 트랙을 넘깁니다.";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (isInGuildMusicCommand(event)) {
            GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
            String resultMsg = "Skip (" +
                    guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo().title +
                    ")";

            guildMusicManager.getTrackScheduler().getPlayer().stopTrack();

            AudioTrack audioTrack = guildMusicManager.
                    getTrackScheduler().
                    getPlayer().
                    getPlayingTrack();

            if (audioTrack != null)
                resultMsg += "\n" + audioTrack.getInfo().uri;

            event.reply(resultMsg).setEphemeral(true).queue();
        }
    }
}
