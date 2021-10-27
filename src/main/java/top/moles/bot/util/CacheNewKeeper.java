package top.moles.bot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public abstract class CacheNewKeeper<T> {
    List<T> cache=new ArrayList<>();
    NewKeeper<T> keeper=new NewKeeper<T>();
    private boolean cache(T e){ if(keeper.have(e))return false;return cache.add(e); }
    public NewKeeper<T> keeper(){return keeper;}
    private void cache(List<T> list){list.forEach(e->cache(e));}
    public abstract List<T> cache();
    private Random random=new Random();
    public T random(){
        while (cache.size()==0)cache(cache());
        T t=cache.remove(random.nextInt(cache.size()));
        keeper.put(t);
        return t;
    }

    public static void main(String[] args) {
        AtomicLong l=new AtomicLong(0);
        CacheNewKeeper<Long> cnk=new CacheNewKeeper<Long>() {
            @Override
            public List<Long> cache() {
                ArrayList list=new ArrayList();
                list.add(l.getAndAdd(1));
                list.add(l.getAndAdd(1));
                return list;
            }
        };
        for (int i = 0; i < 20; i++) {
            System.out.println(cnk.random());
        }
    }
}
