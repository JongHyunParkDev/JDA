package com.yj.yjbot.command.music;

import com.yj.yjbot.command.Command;
import com.yj.yjbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Play implements Command {
    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "유튜브 link 를 주시면 해당 URL sound 를 내보냅니다.";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "링크", "Youtube URL Or !! Youtube Search top one", true));

        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState guildVoiceState = member.getVoiceState();

        if (! guildVoiceState.inAudioChannel()) {
            event.reply("You are not in Voice Channel").setEphemeral(true).queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if (! selfVoiceState.inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(guildVoiceState.getChannel());
        }
        else {
            if (selfVoiceState.getChannel() != guildVoiceState.getChannel()) {
                event.reply("You are not in same channel as Bot").setEphemeral(true).queue();
                return;
            }
        }
        String url = event.getOption("링크").getAsString();
        String msg = url;

        if (url.startsWith("!!")) {
            msg = url.substring(2);
            url = "ytsearch:" + msg;
        }

        PlayerManager playerManager = PlayerManager.get();
        playerManager.play(event.getGuild(), url);
        event.reply(msg).setEphemeral(true).queue();
    }
}
