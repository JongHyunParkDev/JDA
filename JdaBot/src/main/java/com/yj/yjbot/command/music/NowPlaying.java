package com.yj.yjbot.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.yj.yjbot.command.Command;
import com.yj.yjbot.lavaplayer.GuildMusicManager;
import com.yj.yjbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class NowPlaying implements Command {
    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getDescription() {
        return "현재 재생중인 트랙을 보여줍니다.";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (isInGuildMusicCommand(event)) {
            GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
            AudioTrackInfo audioTrackInfo = guildMusicManager.
                    getTrackScheduler().getPlayer().getPlayingTrack().getInfo();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Currently Playing");
            embedBuilder.setDescription("**Name:** `" + audioTrackInfo.title + "`");
            embedBuilder.appendDescription("\n**Author:** `" + audioTrackInfo.author + "`");
            embedBuilder.appendDescription("\n**URL:** `" + audioTrackInfo.uri + "`");

            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        }
    }
}
