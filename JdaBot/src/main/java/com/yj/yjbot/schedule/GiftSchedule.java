package com.yj.yjbot.schedule;


import com.yj.yjbot.data.Gift;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GiftSchedule {
    public static TextChannel textChannel;

    @Scheduled(cron = "0 55 * * * *")
    public void giftTaskSchedule() {
        if (textChannel == null || Gift.GIFT_LIST.isEmpty())
            return;
        if (Gift.GIFT_LIST.peek().getStartHour() - LocalDateTime.now().getHour() == 1) {
            textChannel.sendMessage("다음 선물까지 5분!").queue();
        }
    }
}
