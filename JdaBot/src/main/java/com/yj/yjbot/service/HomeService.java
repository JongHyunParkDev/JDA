package com.yj.yjbot.service;

import ca.tristan.easycommands.commands.EasyCommands;
import ca.tristan.easycommands.commands.HelpCmd;
import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class HomeService {

    @PostConstruct
    private void init() {
        JDABuilder jdaBuilder = JDABuilder.create(token, Arrays.asList(gatewayIntents));

        try {
            JDA jda = jdaBuilder.build().awaitReady();

            EasyCommands easyCommands = new EasyCommands(jda, false);
            easyCommands.addExecutor(new HelpCmd());
            easyCommands.updateCommands();

            jda.addEventListener(easyCommands);
            easyCommands.logCurrentExecutors();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private final GatewayIntent[] gatewayIntents = {
            GatewayIntent.MESSAGE_CONTENT,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MEMBERS
    };

    private final String token = "MTA5NzUxMjg1NjM5MzYyNTY5MA.GNDKYu.k39v5mMBrHiYLSYZEZ8K4w0prnqKNbIlugz8dw";
}
