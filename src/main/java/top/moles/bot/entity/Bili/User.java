package top.moles.bot.entity.Bili;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

@Data
public class User {
    static RestTemplate template;
    static Gson gson;
    static {
        template=new RestTemplate();
        gson=new Gson();
    }
    static String info(Long mid){
        //https://api.bilibili.com/x/space/acc/info?mid=4506341&jsonp=jsonp
        String url=String.format("https://api.bilibili.com/x/space/acc/info?mid=%s&jsonp=jsonp",mid);
        String res = template.getForObject(url, String.class);
        return res;
    }
    static User get(Long mid){
        String s = info(mid);
        return gson.fromJson(gson.fromJson(s, JsonObject.class).get("data"),User.class);
    }

    Long mid;
    String name;
    String face;
    LiveRoom live_room;

    public static void main(String[] args) {
        System.out.println(User.get(4506341L));
    }
}
