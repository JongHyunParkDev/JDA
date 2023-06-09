package com.yj.yjbot.listener;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class EventListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        User user = event.getUser();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channelMention = event.getChannel().getAsMention();
        String jumpLink = event.getJumpUrl();

        String message = user.getAsTag() + " reacted to a message with " + emoji + " in the " + channelMention + " channel!";
        event.getChannel().sendMessage(message).queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (message.contains("ping")) {
            event.getChannel().sendMessage("pong").queue();
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        event.getUser().openPrivateChannel()
                .flatMap(channel -> channel.sendMessage("안녕"))
                .queue();
    }
    // 이건 잠시 지워둔다.
//    @Override
//    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
//        User user = event.getUser();
//        String message = user.getAsTag() + " updated Their online status to " + event.getNewOnlineStatus().getKey();
//        event.getGuild().getDefaultChannel().asTextChannel().sendMessage(message).queue();
//    }
}
