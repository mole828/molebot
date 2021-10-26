package top.moles.bot;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.moles.bot.entity.Pixiv.Pix;

import java.util.List;

@RestController
@RequestMapping("")
public class BotController {
    MongoTemplate mongo;
    BotController(MongoTemplate mongo){
        this.mongo=mongo;
    }
    @RequestMapping("history")
    public List<Pix> history(){
        return mongo.findAll(Pix.class);
    }
    @RequestMapping("history.in")
    public boolean in(@RequestBody Pix pix){
        mongo.insert(pix);
        return true;
    }
}
