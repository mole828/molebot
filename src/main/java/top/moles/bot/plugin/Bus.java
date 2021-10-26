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
            Set<Long> history=new HashSet();
            mongo.find(Query.query(Criteria.where("group").is(group)),Pix.class)
                    .forEach(pix -> history.add(pix.getPid()) );
            log.info(group+" init history: "+history.size());
            handler=new PixivHandler(group,history);
            handlers.put(group,handler);
        }
        return handler;
    }
    Map<Long, Timer> timers=new HashMap<>();
    Timer getTimer(Long qq){
        Timer timer=timers.get(qq);
        if(timer==null){
            timer= new Timer(9, 24 * 60 * 60 * 1000L);
            timers.put(qq,timer);
        }
        return timer;
    }
    @SneakyThrows
    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull OnebotEvent.GroupMessageEvent event) {
        //log.info(event.getRawMessage());
        //https://github.com/botuniverse/onebot-11/blob/master/event/message.md
        PixivHandler handler=get(event.getGroupId());
        if(event.getRawMessage().equals("hello"))
            bot.sendGroupMsg(event.getGroupId(),"hi",false);
        if(event.getRawMessage().startsWith("涩涩")){
            Timer timer=getTimer(event.getUserId());
            if(!timer.next()) {
                bot.sendGroupMsg(event.getGroupId(),String.format("%s,休息一下吧",event.getSender().getCard()),false);
                return MESSAGE_BLOCK;
            }
            Long pid=handler.handle(bot);
            if(pid!=0){
                Pix p = new Pix();p.setGroup(event.getGroupId());p.setPid(pid);
                mongo.insert(p);
                timer.exec();
            }
        }
        if(event.getRawMessage().equals("不可以涩涩()")&&event.getUserId()==1124280866){
            handler.open =!handler.open;
            if(handler.open) bot.sendGroupMsg(event.getGroupId(),"封印解除了",false);
            else bot.sendGroupMsg(event.getGroupId(),Msg.builder().image("https://proxy-jp1.pixivel.moe/c/600x1200_90/img-master/img/2021/10/19/02/53/51/93541556_p3_master1200.jpg"),false);
        }
        if(event.getRawMessage().startsWith("bili()")&&event.getUserId()==1124280866){
            String[] args=event.getRawMessage().split("\\s+");
            if(bili==null)bili=new Handler(bot);
            Long mid=Long.parseLong(args[1]);
            Long group=Long.parseLong(args[2]);
            bili.set(mid,group);
            bot.sendGroupMsg(event.getGroupId(),mid+" "+group,false);
        }
        if(event.getRawMessage().startsWith("test()")&&event.getUserId()==1124280866){
            bot.sendGroupMsg(event.getGroupId(), Msg.builder().image("http://iw233.cn/api/Ghs.php"),false);
        }
        if(event.getRawMessage().startsWith("img")&&event.getUserId()==1124280866){
            String[] args=event.getRawMessage().split("\\s+");
            bot.sendGroupMsg(event.getGroupId(), Msg.builder().image(args[1]),false);
        }
        if(event.getRawMessage().startsWith("pix")&&event.getUserId()==1124280866){
            String[] args=event.getRawMessage().split("\\s+");
            String url = handler.pixivel.url(Long.parseLong(args[1]));

            bot.sendGroupMsg(event.getGroupId(), Msg.builder().text(url),false);
            bot.sendGroupMsg(event.getGroupId(), Msg.builder().image(url),false);
        }
        return super.onGroupMessage(bot, event);
    }


}
