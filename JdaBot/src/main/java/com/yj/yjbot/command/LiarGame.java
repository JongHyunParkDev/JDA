package com.yj.yjbot.command;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LiarGame implements Command{
    @Override
    public String getName() {
        return "liargame";
    }

    @Override
    public String getDescription() {
        return "라이어 게임 (3 ~ 6 인원 가능합니다.)";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "참가1", "게임에 참가할 유저들을 선택해주세요", true));
        options.add(new OptionData(OptionType.USER, "참가2", "게임에 참가할 유저들을 선택해주세요", true));
        options.add(new OptionData(OptionType.USER, "참가3", "게임에 참가할 유저들을 선택해주세요", true));
        options.add(new OptionData(OptionType.USER, "참가4", "게임에 참가할 유저들을 선택해주세요", false));
        options.add(new OptionData(OptionType.USER, "참가5", "게임에 참가할 유저들을 선택해주세요", false));
        options.add(new OptionData(OptionType.USER, "참가6", "게임에 참가할 유저들을 선택해주세요", false));

        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        com.yj.yjbot.data.LiarGame.LiarObj liarObj = com.yj.yjbot.data.LiarGame.getLiarData();
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
