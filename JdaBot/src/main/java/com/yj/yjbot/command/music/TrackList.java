package com.yj.yjbot.command.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.yj.yjbot.command.Command;
import com.yj.yjbot.lavaplayer.GuildMusicManager;
import com.yj.yjbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class TrackList implements Command {
    @Override
    public String getName() {
        return "tracklist";
    }

    @Override
    public String getDescription() {
        return "현재 트랙들의 목록 보여줍니다.";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (isInGuildMusicCommand(event)) {
            GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
            List<AudioTrack> queue = new ArrayList<>(guildMusicManager.getTrackScheduler().getQueue());
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Current Track List");
            if (queue.isEmpty()) {
                embedBuilder.setDescription("Queue is empty");
            }
            for (int i = 0; i < queue.size(); i++) {
                AudioTrackInfo info = queue.get(i).getInfo();
                embedBuilder.addField(i + 1 + " Track Title:", info.title, false);
            }
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        }
    }
}
