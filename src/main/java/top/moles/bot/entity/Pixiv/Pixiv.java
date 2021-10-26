package top.moles.bot.entity.Pixiv;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Pixiv{
    HttpHeaders headers;
    RestTemplate template;
    Pixiv(String userAgent,String cookie){
        headers=new HttpHeaders();
        headers.set("user-agent",userAgent);
        headers.set("cookie",cookie);
        template=new RestTemplate();
    }

    public List<String> rank(String mode) throws URISyntaxException {
        URI uri = new URI(String.format("https://www.pixiv.net/ranking.php?mode=%s",mode));
        HttpHeaders headers=new HttpHeaders();
        headers.set("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36");
        headers.set("cookie","p_ab_id=5; p_ab_id_2=5; p_ab_d_id=1424617013; _ga=GA1.2.181262423.1564191328; PHPSESSID=15271285_f1e3a2f5b750a2283a142bc8d99a9902; a_type=0; b_type=1; login_ever=yes; __utmv=235335808.|2=login%20ever=yes=1^3=plan=normal=1^5=gender=male=1^6=user_id=15271285=1^9=p_ab_id=5=1^10=p_ab_id_2=5=1^11=lang=zh=1; adr_id=Pu5C2gMKawysyCh2zILAK7TvpUWxZ8UfziiSfrbHsxXL84do; auto_view_enabled=1; ki_u=7bb7a90e-8d49-355c-c55f-af8b; _td=573d9654-0678-4329-8975-e87fee013e68; c_type=27; _im_vid=01F60T021QBJ0JQYH0T7G55ZQ1; privacy_policy_notification=0; privacy_policy_agreement=3; first_visit_datetime_pc=2021-07-26+11%3A18%3A34; yuid_b=JFiJOFc; PixivPreview={\"lang\":-1,\"enablePreview\":1,\"enableSort\":1,\"enableAnimeDownload\":1,\"original\":0,\"previewDelay\":200,\"pageCount\":2,\"favFilter\":0,\"hideFavorite\":0,\"hideFollowed\":0,\"linkBlank\":1,\"pageByKey\":0,\"fullSizeThumb\":0,\"logLevel\":1,\"version\":\"3.2.0\"}; __gads=ID=bbc313bae50454bc:T=1621377430:S=ALNI_MaDlQLzKNufBbJlfqO2Mmi9MBThYg; _im_uid.3929=i.NchgDSMHQouOwnE1p6Pjcg; ki_s=202660%3A1.0.0.0.1%3B204128%3A0.0.0.0.0%3B208245%3A0.0.0.0.0%3B209879%3A0.0.0.0.0%3B212334%3A0.0.0.0.2%3B213152%3A0.0.0.0.0%3B214027%3A0.0.0.0.2%3B214908%3A0.0.0.0.2%3B214994%3A0.0.0.0.2%3B215190%3A0.0.0.0.2%3B215821%3A0.0.0.0.2%3B216591%3A0.0.0.0.0%3B217356%3A0.0.0.0.0%3B217998%3A0.0.0.0.2%3B219012%3A0.0.0.0.0%3B219376%3A0.0.0.0.2; __utmz=235335808.1633695000.239.56.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); ki_r=; _gid=GA1.2.660693828.1634575203; _gcl_au=1.1.975880680.1634597587; tag_view_ranking=0xsDLqCEW6~J9DAOECDjo~qWFESUmfEs~_hSAdpN9rx~kWRbcAGDa9~edF4CoWy9T~engSCj5XFq~6293srEnwa~59dAqNEUGJ~vdODHOtIeC~o7hvUrSGDN~-98s6o2-Rp~Ir9TXHbSMT~3mvlXxwzlR~Txs9grkeRc~q_J28dYJ9d~liM64qjhwQ~0AMxxkJq-T~xflXNZNzWe~EZQqoW9r8g~zIv0cf5VVk~38PGY_h86u~1teineLRXQ~e_-YlleiYI~NsbQEogeyL~azESOjmQSV~VbPCYJXdEP~_EOd7bsGyl~DADQycFGB0~gB7nxsLSGV~ehFkU8vOq9~5oPIfUbtd6~RTJMXD26Ak~Lt-oEicbBr~yv-MdmoUJ0~6a53MEkYjr~zUV1dBrslN~0M0zAeslDb~CrFcrMFJzz~MM6RXH_rlN~-CBCDNLJzB~QaiOjmwQnI~HY55MqmzzQ~ZTBAtZUDtQ~q3eUobDMJW~mLrrjwTHBm~2QTW_H5tVX~qXzcci65nj~_vCZ2RLsY2~iifJOK5bFa~y8GNntYHsi~BU9SQkS-zU~Z-FJ6AMFu8~4qunx05BVi~xa5-CDAPro~LLyDB5xskQ~wbvCWCYbkM~-vyZ9ABIqh~821uj_yCkp~n39RQWfHku~UE6jCSAq4S~jk9IzfjZ6n~SgdF0PhjVC~2R7RYffVfj~18TSf2zYVV~Ce-EdaHA-3~KGveC1VuIx~IEelj7HCPz~_6heNC0O-R~So7otvWMNl~yPNaP3JSNF~kqu7T68WD3~Cj_Gcw9KR1~4ZEPYJhfGu~7mUuJlWo2q~w4sebKbvp8~E8plmQ7kUK~us_EbQt5yu~3Ht5EfJAWU~LJo91uBPz4~iNhgXvH5kP~nvsi0opBe_~faHcYIP1U0~BEa426Zwwo~-jxw_sxnha~Ku2rtEHSDm~yTfty1xk17~8p2ehmu0sL~AD8uVcLszs~VN4hvfPTzG~ETjPkL0e6r~52t6xfc7Lh~Hfn05hOINZ~ttxe5RPLoz~tIthLEM_gP~3Q6mwAP5Mi~qWoGAhxrlI~2nSCQQsTc0~uvBGOtCzqF~Lidf9SWNdv; __utma=235335808.181262423.1564191328.1634689460.1634706510.254; __utmc=235335808; __cf_bm=votOX6QibeepL346a7Suu8mVBRHtbZV7LwcB0qA.zX8-1634706511-0-Abs8gdzCoCc2lbin5oloqBCLTUUFPs1mfyEmBFFYqZJH1x5q1xjwOF2F8ocghRd+4xMcXlWru1tdzNfdfZzIKtP+bq/CiUlJxQt4Eq/1fipEIlq+95096j99x/Qpszf+23woAqetKukV6SEYcLq2x5MYr2EjPrE+xO5b7VGNaoa0tQ51lrq+mLFzOrWnISJGBQ==; __utmt=1; ki_t=1564305488258%3B1634687308746%3B1634706513468%3B106%3B923; __utmb=235335808.3.10.1634706510");
        ResponseEntity<String> entity=
                template.exchange(
                        uri,
                        HttpMethod.GET,
                        new HttpEntity<String>(headers),
                        String.class
                );
        Document document= Jsoup.parse(entity.getBody());
        Elements es=document.getElementsByClass("ranking-item");
        ArrayList<String> ids=new ArrayList<>();
        es.forEach(e->{
            String id=e.getAllElements().attr("data-id");
            ids.add(id);
        });
        return ids;
    }

    public static void main(String[] args) throws URISyntaxException {
        Pixiv p=new Pixiv(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36",
                "p_ab_id=5; p_ab_id_2=5; p_ab_d_id=1424617013; _ga=GA1.2.181262423.1564191328; PHPSESSID=15271285_f1e3a2f5b750a2283a142bc8d99a9902; a_type=0; b_type=1; login_ever=yes; __utmv=235335808.|2=login%20ever=yes=1^3=plan=normal=1^5=gender=male=1^6=user_id=15271285=1^9=p_ab_id=5=1^10=p_ab_id_2=5=1^11=lang=zh=1; adr_id=Pu5C2gMKawysyCh2zILAK7TvpUWxZ8UfziiSfrbHsxXL84do; auto_view_enabled=1; ki_u=7bb7a90e-8d49-355c-c55f-af8b; _td=573d9654-0678-4329-8975-e87fee013e68; c_type=27; _im_vid=01F60T021QBJ0JQYH0T7G55ZQ1; privacy_policy_notification=0; privacy_policy_agreement=3; first_visit_datetime_pc=2021-07-26+11%3A18%3A34; yuid_b=JFiJOFc; PixivPreview={\"lang\":-1,\"enablePreview\":1,\"enableSort\":1,\"enableAnimeDownload\":1,\"original\":0,\"previewDelay\":200,\"pageCount\":2,\"favFilter\":0,\"hideFavorite\":0,\"hideFollowed\":0,\"linkBlank\":1,\"pageByKey\":0,\"fullSizeThumb\":0,\"logLevel\":1,\"version\":\"3.2.0\"}; __gads=ID=bbc313bae50454bc:T=1621377430:S=ALNI_MaDlQLzKNufBbJlfqO2Mmi9MBThYg; _im_uid.3929=i.NchgDSMHQouOwnE1p6Pjcg; ki_s=202660%3A1.0.0.0.1%3B204128%3A0.0.0.0.0%3B208245%3A0.0.0.0.0%3B209879%3A0.0.0.0.0%3B212334%3A0.0.0.0.2%3B213152%3A0.0.0.0.0%3B214027%3A0.0.0.0.2%3B214908%3A0.0.0.0.2%3B214994%3A0.0.0.0.2%3B215190%3A0.0.0.0.2%3B215821%3A0.0.0.0.2%3B216591%3A0.0.0.0.0%3B217356%3A0.0.0.0.0%3B217998%3A0.0.0.0.2%3B219012%3A0.0.0.0.0%3B219376%3A0.0.0.0.2; __utmz=235335808.1633695000.239.56.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); ki_r=; _gid=GA1.2.660693828.1634575203; _gcl_au=1.1.975880680.1634597587; tag_view_ranking=0xsDLqCEW6~J9DAOECDjo~qWFESUmfEs~_hSAdpN9rx~kWRbcAGDa9~edF4CoWy9T~engSCj5XFq~6293srEnwa~59dAqNEUGJ~vdODHOtIeC~o7hvUrSGDN~-98s6o2-Rp~Ir9TXHbSMT~3mvlXxwzlR~Txs9grkeRc~q_J28dYJ9d~liM64qjhwQ~0AMxxkJq-T~xflXNZNzWe~EZQqoW9r8g~zIv0cf5VVk~38PGY_h86u~1teineLRXQ~e_-YlleiYI~NsbQEogeyL~azESOjmQSV~VbPCYJXdEP~_EOd7bsGyl~DADQycFGB0~gB7nxsLSGV~ehFkU8vOq9~5oPIfUbtd6~RTJMXD26Ak~Lt-oEicbBr~yv-MdmoUJ0~6a53MEkYjr~zUV1dBrslN~0M0zAeslDb~CrFcrMFJzz~MM6RXH_rlN~-CBCDNLJzB~QaiOjmwQnI~HY55MqmzzQ~ZTBAtZUDtQ~q3eUobDMJW~mLrrjwTHBm~2QTW_H5tVX~qXzcci65nj~_vCZ2RLsY2~iifJOK5bFa~y8GNntYHsi~BU9SQkS-zU~Z-FJ6AMFu8~4qunx05BVi~xa5-CDAPro~LLyDB5xskQ~wbvCWCYbkM~-vyZ9ABIqh~821uj_yCkp~n39RQWfHku~UE6jCSAq4S~jk9IzfjZ6n~SgdF0PhjVC~2R7RYffVfj~18TSf2zYVV~Ce-EdaHA-3~KGveC1VuIx~IEelj7HCPz~_6heNC0O-R~So7otvWMNl~yPNaP3JSNF~kqu7T68WD3~Cj_Gcw9KR1~4ZEPYJhfGu~7mUuJlWo2q~w4sebKbvp8~E8plmQ7kUK~us_EbQt5yu~3Ht5EfJAWU~LJo91uBPz4~iNhgXvH5kP~nvsi0opBe_~faHcYIP1U0~BEa426Zwwo~-jxw_sxnha~Ku2rtEHSDm~yTfty1xk17~8p2ehmu0sL~AD8uVcLszs~VN4hvfPTzG~ETjPkL0e6r~52t6xfc7Lh~Hfn05hOINZ~ttxe5RPLoz~tIthLEM_gP~3Q6mwAP5Mi~qWoGAhxrlI~2nSCQQsTc0~uvBGOtCzqF~Lidf9SWNdv; __utma=235335808.181262423.1564191328.1634689460.1634706510.254; __utmc=235335808; __cf_bm=votOX6QibeepL346a7Suu8mVBRHtbZV7LwcB0qA.zX8-1634706511-0-Abs8gdzCoCc2lbin5oloqBCLTUUFPs1mfyEmBFFYqZJH1x5q1xjwOF2F8ocghRd+4xMcXlWru1tdzNfdfZzIKtP+bq/CiUlJxQt4Eq/1fipEIlq+95096j99x/Qpszf+23woAqetKukV6SEYcLq2x5MYr2EjPrE+xO5b7VGNaoa0tQ51lrq+mLFzOrWnISJGBQ==; __utmt=1; ki_t=1564305488258%3B1634687308746%3B1634706513468%3B106%3B923; __utmb=235335808.3.10.1634706510"
        );
        SimpleClientHttpRequestFactory reqfac = new SimpleClientHttpRequestFactory();
        reqfac.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10809)));
        p.template.setRequestFactory(reqfac);
        p.rank("male").forEach(s->System.out.println(s));
    }
}
