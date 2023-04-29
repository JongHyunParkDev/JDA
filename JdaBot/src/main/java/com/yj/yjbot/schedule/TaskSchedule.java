package com.yj.yjbot.schedule;


import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskSchedule {
    public static TextChannel textChannel;

//    @Scheduled(cron = "0 55 * * * *")
//    public void taskSchedule() {
//        if (textChannel == null)
//            return;
//        // run Task
//    }

    private static final Logger logger = LoggerFactory.getLogger(TaskSchedule.class);
}
