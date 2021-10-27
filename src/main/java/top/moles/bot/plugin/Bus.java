package top.moles.bot.plugin;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.bot.BotPlugin;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import top.moles.bot.entity.ColdDown;
import top.moles.bot.entity.Pixiv.Pix;
import top.moles.bot.entity.Bili.Handler;
import top.moles.bot.entity.Pixiv.PixivHandler;
import top.moles.bot.entity.Timer;

import java.util.*;

@Slf4j
@Component
public class Bus extends BotPlugin {
    MongoTemplate mongo;
    Bus(MongoTemplate mongo){
        this.mongo=mongo;
    }
    Handler bili;
    Map<Long,PixivHandler> handlers=new HashMap<>();
    PixivHandler get(Long group){
        PixivHandler handler=handlers.get(group);
        if(handler==null){
            handler=new PixivHandler(group,mongo);
            handlers.put(group,handler);
        }
        return handler;
    }
    @SneakyThrows
    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull OnebotEvent.GroupMessageEvent event) {
        //log.info(event.getRawMessage());
        //https://github.com/botuniverse/onebot-11/blob/master/event/message.md
        PixivHandler handler=get(event.getGroupId());
        if(event.getRawMessage().equals("hello"))bot.sendGroupMsg(event.getGroupId(),"hi",false);
        if(event.getRawMessage().startsWith("涩涩")){
            handler.handle(bot,event);
        }


        if(event.getRawMessage().startsWith("bili()")&&event.getUserId()==1124280866){
            String[] args=event.getRawMessage().split("\\s+");
            if(bili==null)bili=new Handler(bot);
            Long mid=Long.parseLong(args[1]);
            Long group=Long.parseLong(args[2]);
            bili.set(mid,group);
            bot.sendGroupMsg(event.getGroupId(),mid+" "+group,false);
        }

        if(event.getRawMessage().startsWith("img")&&event.getUserId()==1124280866){
            String[] args=event.getRawMessage().split("\\s+");
            bot.sendGroupMsg(event.getGroupId(), Msg.builder().image(args[1]),false);
        }
        return MESSAGE_IGNORE;
    }


}
