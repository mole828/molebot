package top.moles.bot.entity.Pixiv;

import lombok.extern.slf4j.Slf4j;
import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import top.moles.bot.entity.ColdDown;
import top.moles.bot.entity.Timer;
import top.moles.bot.util.CacheNewKeeper;

import java.util.*;

@Slf4j
public class PixivHandler {
    MongoTemplate mongo;
    Long group;
    CacheNewKeeper<Long> cnk;
    Pixivel pixivel=new Pixivel();
    public PixivHandler(Long group, MongoTemplate mongo){
        this.mongo  =mongo;
        this.group  =group;
        this.cnk=new CacheNewKeeper<Long>() {
            int page=0;
            @Override
            public List cache() {
                try {
                    return pixivel.rec(page++);
                } catch (Exception e) { e.printStackTrace(); }
                return null;
            }
        };
        List<Pix> list=mongo.find(Query.query(Criteria.where("group").is(group)),Pix.class);
        list.forEach(p->{ if(!cnk.keeper().put(p.getPid()))mongo.remove(p); });
        log.info(String.format("%s init history: %s",group,cnk.keeper().history().get().size()));
        log.info(String.format("%s clean history: %s",group,list.size()-cnk.keeper().history().get().size()));
    }
    ColdDown cd=new ColdDown(5000L);
    Map<Long,Timer> timers=new HashMap<>();
    Timer timer(Long uid){
        Timer timer=timers.get(uid);
        if(timer==null){timer=new Timer(9,8*60*60*1000L);timers.put(uid,timer);}
        return timer;
    }
    public void handle(Bot bot, OnebotEvent.GroupMessageEvent event) {
        if(!cd.ready()){
            bot.sendGroupMsg(event.getGroupId(),
                    String.format("涩涩冷却中。",event.getSender().getCard()),
                    false);
            return;
        }cd.exec();
        Timer t=timer(event.getUserId());
        if(!t.next()){
            bot.sendGroupMsg(event.getGroupId(),
                    String.format("%s,休息一下吧，去看个书好不好",event.getSender().getCard()),
                    false);
            return;
        }t.exec();

        Long id=cnk.random();
        String mrl = pixivel.url(id);
        String prl = String.format("https://www.pixiv.net/artworks/%s",id);
        log.info(String.format("url: %s",mrl));
        Msg.builder().text(prl).image(mrl).sendToGroup(bot,group);
        Pix p=new Pix();p.setGroup(group);p.setPid(id);
        mongo.insert(p);
    }
}
