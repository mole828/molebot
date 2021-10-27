package top.moles.bot.util;

import java.util.HashSet;
import java.util.Set;

public class History<T> {
    Set<T> history=new HashSet<>();
    public boolean put(T e){
        return history.add(e);
    }
    public Set<T> get(){return history;}
}
