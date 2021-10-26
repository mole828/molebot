package top.moles.bot.entity.Pixiv;

import lombok.extern.slf4j.Slf4j;
import net.lz1998.pbbot.bot.Bot;
import net.lz1998.pbbot.utils.Msg;
import onebot.OnebotBase;
import top.moles.bot.entity.ColdDown;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
public class PixivHandler {
    Long group;
    Set<Long> history;
    public PixivHandler(Long group,Set<Long> history){
        this.group  =group;
        this.history=history;
    }
    List<Long> cache    =   new ArrayList<>();
    public Pixivel pixivel     =   new Pixivel();int page=0;
    Random random       =   new Random();
    public Boolean open =   true;
    ColdDown cd         =   new ColdDown(5000L);
    public Long handle(Bot bot) throws IOException, URISyntaxException {
        {
            if(!open){
                bot.sendGroupMsg(group,"涩涩被封印了",false);
                return 0L;
            }
            if (!cd.ready()) {
                bot.sendGroupMsg(group, "涩涩冷却中...", false);
                return 0L;
            }
            cd.exec();
        }
        while(cache.size()==0){
            pixivel.rec(page).forEach(id->{
                if(!history.contains(id))cache.add(id);
            });
            page+=1;
        }
        Long id=cache.remove(random.nextInt(cache.size()));
        history.add(id);
        String mrl = pixivel.url(id);
        String prl = String.format("https://www.pixiv.net/artworks/%s",id);
        log.info(String.format("url: %s",mrl));
        Msg.builder().text(prl).image(mrl).sendToGroup(bot,group);
        return id;
    }
}
