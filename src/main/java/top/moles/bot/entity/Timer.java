package top.moles.bot.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Timer {
    int max;
    long d;
    public Timer(int max, long d){
        this.max=max;
        this.d=d;
    }
    List<Long> list=new ArrayList<>();
    public boolean next(){
        if(list.size()<max)return true;
        long now=new Date().getTime();
        long m=list.get(max-1);
        if(now>m+d)return true;
        return false;
    }
    public void exec(){
        list.add(0,new Date().getTime());
        if(list.size()>max)list.remove(max);
    }
}
