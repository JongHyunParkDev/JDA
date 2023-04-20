package com.yj.yjbot.command;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class LiarGameCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("liargame")) {
            String result = String.format("라이어 게임 (카테고리 - %s)", "테스트");
            event.reply(result).queue();
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(
            new ArrayList<>() {{
                add(Commands.slash("liargame", "라이어 게임"));
            }}
        ).queue();
    }

    // TODO
    // JSON 파일을 읽어서 관리하자. 뭔가 DB 쓰면 배꼽이 배보다 클 것 같다...
}
