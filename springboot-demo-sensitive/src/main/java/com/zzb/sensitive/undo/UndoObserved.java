package com.zzb.sensitive.undo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

/**
 * 被观察者
 */
@Component
public class UndoObserved extends Observable {

    private static boolean IS_OBSERVE;

    /*观察者列表*/
    private List<Observer> observerList;

    @PostConstruct
    public void observerRegister() {
        if (observerList != null && !observerList.isEmpty()) {
            observerList.stream().filter(f -> f instanceof com.zzb.sensitive.undo.UndoObserver).forEach(this::addObserver);
            if (observerList.stream().anyMatch(f -> f instanceof com.zzb.sensitive.undo.UndoObserver)) {
                observerList = observerList.stream().filter(f -> f instanceof com.zzb.sensitive.undo.UndoObserver).collect(Collectors.toList());
                IS_OBSERVE = true;
            }
        }
    }

    /**
     * 方法：sendResult
     * 描述：广播信息
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param vo :
     * @return : void
     * @date: 2020年12月15日 1:19 下午
     */
    public synchronized void sendResult(com.zzb.sensitive.undo.UndoVO vo) {

        this.setChanged();
        this.notifyObservers(vo);
    }

    /**
     * 方法：isObserver
     * 描述：是否有观察者
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : boolean
     * @date: 2020年12月15日 1:19 下午
     */
    public static boolean isObserver() {
        return IS_OBSERVE;
    }

    /**
     * 方法：getObserverList
     * 描述：获取所有注册的观察者
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : java.util.List<java.util.Observer>
     * @date: 2020年12月15日 1:20 下午
     */
    public List<Observer> getObserverList() {
        return observerList;
    }

    @Autowired(required = false)
    public void setObserverList(List<Observer> observerList) {
        this.observerList = observerList;
    }
}
