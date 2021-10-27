package top.moles.bot.util;

public class NewKeeper<T> {
    History<T> history=new History<>();
    public boolean put(T e){return history.put(e);}
    public boolean have(T e){return history.history.contains(e);}
    public History<T> history(){return history;}
}
