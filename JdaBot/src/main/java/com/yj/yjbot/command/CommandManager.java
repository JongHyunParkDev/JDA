package com.yj.yjbot.command;

import com.yj.yjbot.data.Gift;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
            String result = String.format("라이어 게임 (카테고리 - %s)", "테스트");
            Guild guild = event.getGuild();

            guild.loadMembers()
                .onSuccess(members -> {
                    List<Member> onlineMembers = members.stream()
                        .filter(
                            member ->
                                member.getOnlineStatus() == OnlineStatus.ONLINE && ! member.getUser().isBot())
                        .collect(Collectors.toList());

                    onlineMembers.forEach(member -> {
                        member.getUser().openPrivateChannel().queue(
                            privateChannel -> {
                                privateChannel.sendMessage("너는 라이어야").queue();
                            });
                    });
                })
                .onError(error -> {
                    // 멤버 목록 로딩에 실패한 경우 처리
                    event.getHook().sendMessage("Failed to load members.").queue();
                });
            event.reply(result).queue();
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(
                new ArrayList<>() {{
                    add(Commands.slash("gift", "선물!!!"));
                    add(Commands.slash("liargame", "라이어 게임"));
                }}
        ).queue();
    }
}
