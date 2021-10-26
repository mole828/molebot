package top.moles.bot.entity;

import java.util.Date;

public class ColdDown {
    Long last;
    Long d;
    public ColdDown(Long d){
        this.last=0L;
        this.d=d;
    }
    public void exec(){last=new Date().getTime();}
    public boolean ready(){
        return new Date().getTime()>last+d;
    }
}
