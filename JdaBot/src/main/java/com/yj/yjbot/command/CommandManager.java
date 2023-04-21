package com.yj.yjbot.command;

import com.yj.yjbot.data.Gift;
import com.yj.yjbot.data.LiarGame;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

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
        // TODO 참가 기능을 만들어서 온라인 유저 전부에게 보내는 것이 아닌, 참가된 유저에 한해서 만 보낸다.
        else if (command.equals("liargame")) {
            LiarGame.LiarObj liarObj = LiarGame.getLiarData();
            String result = String.format("라이어 게임 (카테고리 - %s)", liarObj.getCategory());
            Guild guild = event.getGuild();

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
        event.getGuild().updateCommands().addCommands(
                new ArrayList<>() {{
                    add(Commands.slash("gift", "선물!!!"));
                    add(Commands.slash("liargame", "라이어 게임 (최소 3명 이상 가능합니다.)")
                            .addOption(OptionType.USER, "참가1", "게임에 참가할 유저들을 선택해주세요", true)
                            .addOption(OptionType.USER, "참가2", "게임에 참가할 유저들을 선택해주세요", true)
                            .addOption(OptionType.USER, "참가3", "게임에 참가할 유저들을 선택해주세요", true)
                            .addOption(OptionType.USER, "참가4", "게임에 참가할 유저들을 선택해주세요", false)
                            .addOption(OptionType.USER, "참가5", "게임에 참가할 유저들을 선택해주세요", false)
                            .addOption(OptionType.USER, "참가6", "게임에 참가할 유저들을 선택해주세요", false));
                }}
        ).queue();
    }
}
