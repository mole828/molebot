package top.moles.bot.entity.Bili;

import lombok.extern.slf4j.Slf4j;
import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.utils.Msg;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Handler {
    Bot bot;
    Map<Long, List<Long>> map;
    public Handler(Bot bot){
        this.bot=bot;
        this.map=new HashMap<>();
    }
    public void set(Long mid,Long group){
        List list=map.getOrDefault(mid,new ArrayList<>());
        if(!map.containsKey(mid)){
            map.put(mid,list);

            new Thread(()->{
                int last=User.get(mid).live_room.liveStatus;
                AtomicInteger i=new AtomicInteger(0);
                while(true){
                    try {
                        Thread.sleep(10*1000);
                    } catch (InterruptedException e) { }

                    if(i.addAndGet(1)==60){
                        i.set(0);
                        log.info("beat mid: "+mid);
                    }
                    {
                        User user = User.get(mid);
                        LiveRoom live_room=user.live_room;
                        int now = user.live_room.liveStatus;
                        if (now != last && now==1) {
                            Msg msg=Msg.builder()
                                    .text(live_room.title)
                                    .text(live_room.url)
                                    .image(live_room.cover)
                                    .text("开始直播了");
                            this.map.get(mid).forEach(g->{
                                bot.sendGroupMsg(g,msg,false);
                            });
                        }
                        last = now;
                    }
                }
            }).start();
        }
        if(list.contains(group))list.remove(group);
        else list.add(group);
    }
}
