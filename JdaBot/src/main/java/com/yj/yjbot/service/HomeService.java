package com.yj.yjbot.service;

import com.yj.yjbot.command.CommandManager;
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
        builder.setActivity(Activity.playing("YM 생일을 JH 와 함께 축하"));
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

        shardManager = builder.build();

        // Register Listener
        shardManager.addEventListener(
                new EventListener(),
                new CommandManager()
        );
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    @Value("${jda.bot.token}")
    private String token;
}
