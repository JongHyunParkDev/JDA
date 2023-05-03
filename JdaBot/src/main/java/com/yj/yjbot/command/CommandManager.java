package com.yj.yjbot.command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yj.yjbot.data.LiarGame;
import com.yj.yjbot.lavaplayer.GuildMusicManager;
import com.yj.yjbot.lavaplayer.PlayerManager;
import com.yj.yjbot.lavaplayer.TrackScheduler;
import com.yj.yjbot.schedule.TaskSchedule;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommandManager extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("say")) {
            String message = event.getOption("메시지").getAsString();

            MessageChannel channel;
            OptionMapping channelOption = event.getOption("채널");

            if (channelOption == null) {
                channel = event.getChannel();
            }
            else {
                channel = channelOption.getAsChannel().asTextChannel();
            }

            channel.sendMessage(message).queue();
            event.reply("메시지가 전달되었습니다.").setEphemeral(true).queue();
        }
        else if (command.equals("liargame")) {
            LiarGame.LiarObj liarObj = LiarGame.getLiarData();
            String result = String.format("라이어 게임 (카테고리 - %s)", liarObj.getCategory());

            List<Member> memberList = new ArrayList<>();
            // 최대 6 명
            for (int i = 1; i <= 6; i++) {
                OptionMapping optionMapping = event.getOption("참가" + i);
                if (optionMapping != null) {
                    memberList.add(optionMapping.getAsMember());
                }
            }

            int random = new Random().nextInt(memberList.size());

            for (int i = 0; i < memberList.size(); i++) {
                String text = random == i ? "당신은 라이어 입니다." : String.format("단어는 %s 입니다.", liarObj.getAnswer());
                memberList.get(i).getUser().openPrivateChannel().queue(
                    privateChannel -> {
                        privateChannel.sendMessage(text).queue();
                    }
                );
            }
            event.reply(result).queue();
        }
        else if (command.equals("play")) {
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
        else if (command.equals("skip")) {
            Member member = event.getMember();
            GuildVoiceState guildVoiceState = member.getVoiceState();

            if (! guildVoiceState.inAudioChannel()) {
                event.reply("You are not in Voice Channel").setEphemeral(true).queue();
                return;
            }

            Member self = event.getGuild().getSelfMember();
            GuildVoiceState selfVoiceState = self.getVoiceState();

            if (! selfVoiceState.inAudioChannel()) {
                event.reply("I (Bot) am not in an audio channel").setEphemeral(true).queue();
                return;
            }

            if (selfVoiceState.getChannel() != guildVoiceState.getChannel()) {
                event.reply("You are not in same channel as Bot").setEphemeral(true).queue();
                return;
            }
            GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
            String resultMsg = "Skipped (" +
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
        else if (command.equals("clear")) {
            Member member = event.getMember();
            GuildVoiceState guildVoiceState = member.getVoiceState();

            if (! guildVoiceState.inAudioChannel()) {
                event.reply("You are not in Voice Channel").setEphemeral(true).queue();
                return;
            }

            Member self = event.getGuild().getSelfMember();
            GuildVoiceState selfVoiceState = self.getVoiceState();

            if (! selfVoiceState.inAudioChannel()) {
                event.reply("I (Bot) am not in an audio channel").setEphemeral(true).queue();
                return;
            }

            if (selfVoiceState.getChannel() != guildVoiceState.getChannel()) {
                event.reply("You are not in same channel as Bot").setEphemeral(true).queue();
                return;
            }
            GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
            TrackScheduler trackScheduler = guildMusicManager.getTrackScheduler();
            trackScheduler.getQueue().clear();
            trackScheduler.getPlayer().stopTrack();

            event.reply("Clear").setEphemeral(true).queue();
        }
        else if (command.equals("pause")) {
            Member member = event.getMember();
            GuildVoiceState guildVoiceState = member.getVoiceState();

            if (! guildVoiceState.inAudioChannel()) {
                event.reply("You are not in Voice Channel").setEphemeral(true).queue();
                return;
            }

            Member self = event.getGuild().getSelfMember();
            GuildVoiceState selfVoiceState = self.getVoiceState();

            if (! selfVoiceState.inAudioChannel()) {
                event.reply("I (Bot) am not in an audio channel").setEphemeral(true).queue();
                return;
            }

            if (selfVoiceState.getChannel() != guildVoiceState.getChannel()) {
                event.reply("You are not in same channel as Bot").setEphemeral(true).queue();
                return;
            }
            GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
            TrackScheduler trackScheduler = guildMusicManager.getTrackScheduler();
            trackScheduler.getPlayer().setPaused(true);

            event.reply("Pause").setEphemeral(true).queue();
        }
        else if (command.equals("resume")) {
            Member member = event.getMember();
            GuildVoiceState guildVoiceState = member.getVoiceState();

            if (! guildVoiceState.inAudioChannel()) {
                event.reply("You are not in Voice Channel").setEphemeral(true).queue();
                return;
            }

            Member self = event.getGuild().getSelfMember();
            GuildVoiceState selfVoiceState = self.getVoiceState();

            if (! selfVoiceState.inAudioChannel()) {
                event.reply("I (Bot) am not in an audio channel").setEphemeral(true).queue();
                return;
            }

            if (selfVoiceState.getChannel() != guildVoiceState.getChannel()) {
                event.reply("You are not in same channel as Bot").setEphemeral(true).queue();
                return;
            }
            GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
            TrackScheduler trackScheduler = guildMusicManager.getTrackScheduler();
            trackScheduler.getPlayer().setPaused(false);

            event.reply("Resume").setEphemeral(true).queue();
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        TaskSchedule.textChannel = event.getGuild().getDefaultChannel().asTextChannel();

        event.getGuild().updateCommands().addCommands(
                new ArrayList<>() {{
                    add(Commands.slash("say", "입력 받은 값을 그대로 Bot이 쳐줍니다.")
                            .addOptions(
                                new OptionData(OptionType.STRING, "메시지", "전송할 메시지", true),
                                new OptionData(OptionType.CHANNEL, "채널", "Message 채널 선택", false)
                                        .setChannelTypes(ChannelType.TEXT, ChannelType.NEWS)
                            ));
                    add(Commands.slash("liargame", "라이어 게임 (3 ~ 6 인원 가능합니다.)")
                            .addOptions(
                                new OptionData(OptionType.USER, "참가1", "게임에 참가할 유저들을 선택해주세요", true),
                                new OptionData(OptionType.USER, "참가2", "게임에 참가할 유저들을 선택해주세요", true),
                                new OptionData(OptionType.USER, "참가3", "게임에 참가할 유저들을 선택해주세요", true),
                                new OptionData(OptionType.USER, "참가4", "게임에 참가할 유저들을 선택해주세요", false),
                                new OptionData(OptionType.USER, "참가5", "게임에 참가할 유저들을 선택해주세요", false),
                                new OptionData(OptionType.USER, "참가6", "게임에 참가할 유저들을 선택해주세요", false)
                            ));
                    add(Commands.slash("play", "유튜브 link 를 주시면 해당 URL sound 를 내보냅니다.")
                            .addOptions(
                                    new OptionData(OptionType.STRING, "링크", "Youtube URL Or !! Youtube Search top one", true)
                            ));
                    add(Commands.slash("skip", "현재 재생중인 트랙을 넘깁니다."));
                    add(Commands.slash("clear", "현재 트랙들을 모두 지웁니다."));
                    add(Commands.slash("queue", "현재 재생중 or 재생 예정인 목록 보여줍니다."));
                    add(Commands.slash("repeat", "현재 재생중인 트랙을 반복합니다."));
                    add(Commands.slash("nowplaying", "현재 재생중인 트랙을 보여줍니다."));
                    add(Commands.slash("pause", "현재 재생중인 트랙을 정지합니다."));
                    add(Commands.slash("resume", "현재 정지된 트랙을 재실행합니다."));
                }}
        ).queue();
    }
}
