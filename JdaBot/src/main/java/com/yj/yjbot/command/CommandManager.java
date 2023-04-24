package com.yj.yjbot.command;

import com.yj.yjbot.data.Gift;
import com.yj.yjbot.data.LiarGame;
import com.yj.yjbot.schedule.GiftSchedule;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import javax.swing.text.html.Option;
import java.nio.channels.Channel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CommandManager extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("gift")) {
            LocalDateTime now = LocalDateTime.now();
            int hour = now.getHour();

            if (now.isAfter(Gift.START_DATE_TIME) && now.isBefore(Gift.END_DATE_TIME) && ! Gift.GIFT_LIST.isEmpty()) {
                Gift gift = Gift.GIFT_LIST.peek();
                if (gift.getStartHour() <= hour) {
                    Gift.GIFT_LIST.remove();
                    event.reply(gift.getDiscordText()).queue();
                    return;
                }
                event.reply(String.format("선물 시간이 아니다!. 다음 선물 시각은 %d시", gift.getStartHour())).queue();
                return;
            }
            event.reply("선물을 받을 수 없는 날짜 이거나, 선물이 소진되었습니다.").queue();
        }
        else if (command.equals("say")) {
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
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        GiftSchedule.textChannel = event.getGuild().getDefaultChannel().asTextChannel();

        event.getGuild().updateCommands().addCommands(
                new ArrayList<>() {{
                    add(Commands.slash("gift", "선물!!!"));
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
                }}
        ).queue();
    }
}
