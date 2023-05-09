package com.yj.yjbot.service;

import com.yj.yjbot.command.CommandManager;
import com.yj.yjbot.command.LiarGame;
import com.yj.yjbot.command.Say;
import com.yj.yjbot.command.music.*;
import com.yj.yjbot.listener.EventListener;
import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HomeService {
    private ShardManager shardManager;

    @PostConstruct
    private void init() {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("공부"));
        builder.enableIntents(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_PRESENCES
        );
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY);

        CommandManager manager = new CommandManager();
        manager.add(new Clear());
        manager.add(new NowPlaying());
        manager.add(new Pause());
        manager.add(new Play());
        manager.add(new Resume());
        manager.add(new Skip());
        manager.add(new TrackList());
        manager.add(new LiarGame());
        manager.add(new Say());

        shardManager = builder.build();

        // Register Listener
        shardManager.addEventListener(manager);
        shardManager.addEventListener(new EventListener());

//        JDA jda = JDABuilder.createDefault(token).build();
//        jda.addEventListener(new EventListener());
//        CommandManager manager = new CommandManager();
//        manager.add(new Clear());
//        manager.add(new NowPlaying());
//        manager.add(new Pause());
//        manager.add(new Play());
//        manager.add(new Resume());
//        manager.add(new Skip());
//        manager.add(new TrackList());
//        manager.add(new LiarGame());
//        manager.add(new Say());
//        jda.addEventListener(manager);
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    @Value("${jda.bot.token}")
    private String token;
}
