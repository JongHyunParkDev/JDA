package com.yj.yjbot.command;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Say implements Command{
    @Override
    public String getName() {
        return "say";
    }

    @Override
    public String getDescription() {
        return "입력 받은 값을 그대로 Bot이 쳐줍니다.";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<OptionData>();
        options.add(new OptionData(OptionType.STRING, "메시지", "전송할 메시지", true));
        options.add(new OptionData(OptionType.CHANNEL, "채널", "Message 채널 선택", false)
                .setChannelTypes(ChannelType.TEXT, ChannelType.NEWS));
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
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
}
