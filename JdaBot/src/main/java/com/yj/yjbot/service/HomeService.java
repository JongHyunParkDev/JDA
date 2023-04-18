package com.yj.yjbot.service;

import ca.tristan.easycommands.commands.EasyCommands;
import ca.tristan.easycommands.commands.HelpCmd;
import com.yj.yjbot.command.SayCmd;
import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class HomeService {

    @PostConstruct
    private void init() {
        JDABuilder jdaBuilder = JDABuilder.create(token, Arrays.asList(EasyCommands.gatewayIntents));

        jdaBuilder.enableCache(Arrays.asList(EasyCommands.cacheFlags));

        try {
            JDA jda = jdaBuilder.build().awaitReady();

            EasyCommands easyCommands = new EasyCommands(jda, false);
            easyCommands.addExecutor(new HelpCmd(easyCommands), new SayCmd());
            easyCommands.enableMusicBot();
//            easyCommands.registerListeners(
//
//            );

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private final String token = "MTA5NzUxMjg1NjM5MzYyNTY5MA.GMUCtq.XEgV6IM-xJdg-RzCghyKvWq-5hwL1drmUfr_hQ";
}
