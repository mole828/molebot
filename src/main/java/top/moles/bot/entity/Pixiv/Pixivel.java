package top.moles.bot.entity.Pixiv;

import com.google.gson.*;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Pixivel{
    static String api="api.pixivel.moe";//api-jp1.pixivel.moe
    static String proxy="proxy-jp1.pixivel.moe";
    static String host=String.format("https://%s/",api);
    HttpHeaders headers;
    RestTemplate template;
    Gson gson;
    public Pixivel(){
        headers=new HttpHeaders();
        template=new RestTemplate();
        gson=new Gson();
    }

    public static String info(Long id) {
        String url=String.format(host+"pixiv?type=illust&id=%s",id);
        ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Scanner input= null;
        try {
            input = new Scanner(requestFactory.createRequest(new URI(url), HttpMethod.GET).execute().getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder builder=new StringBuilder();
        while (input.hasNext()) builder.append(input.nextLine());
        return builder.toString();
    }
    //https://api-jp1.pixivel.moe/pixiv?type=illust&id=90380083
    public String url(Long id) {
        JsonElement json=gson.fromJson(info(id), JsonElement.class);
        JsonObject illust = json.getAsJsonObject().getAsJsonObject("illust");
        JsonArray meta_pages=illust.getAsJsonArray("meta_pages");
        String url=null;
        if(meta_pages.size()==0){
            url=illust.getAsJsonObject("meta_single_page").get("original_image_url").getAsString();
        }else {
            url=meta_pages.get(0).getAsJsonObject().getAsJsonObject("image_urls").get("original").getAsString();
        }
        return url.replaceAll("i.pximg.net",proxy);
    }

    //https://api-jp1.pixivel.moe/pixiv?type=illust_recommended
    public List<Long> rec(int page) throws URISyntaxException, IOException {
        String url=String.format("https://api-jp1.pixivel.moe/pixiv?type=illust_recommended&page="+page);
        ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Scanner input=new Scanner(requestFactory.createRequest(new URI(url),HttpMethod.GET).execute().getBody());
        StringBuilder builder=new StringBuilder();
        while (input.hasNext()) builder.append(input.nextLine());
        String s= builder.toString();
        //String s= template.getForObject(url,String.class);
        JsonElement json=gson.fromJson(s, JsonElement.class);
        List<Long> list=new ArrayList<>();
        json.getAsJsonObject().getAsJsonArray("illusts").forEach(e->{
            list.add(e.getAsJsonObject().get("id").getAsLong());
        });
        return list;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        //System.out.println(new Pixivel().url(90380083L));
        //System.out.println(new Pixivel().url(91318819L));
        Pixivel p = new Pixivel();
        //SimpleClientHttpRequestFactory reqfac = new SimpleClientHttpRequestFactory();
        //reqfac.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10809)));
        //p.template.setRequestFactory(reqfac);
        String url=p.url(92731384L);
        System.out.println(url);

    }
}
