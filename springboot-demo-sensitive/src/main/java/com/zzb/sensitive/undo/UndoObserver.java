package com.zzb.sensitive.undo;


import java.util.Observable;
import java.util.Observer;

/**
 * 类名称：UndoObserver
 * 类描述：观察者处理对象
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 12:16 下午
 * 修改备注：TODO
 */
public abstract class UndoObserver implements Observer {


    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof UndoObserved) || !(arg instanceof com.zzb.sensitive.undo.UndoVO)) {
            return;
        }
        undoValue((com.zzb.sensitive.undo.UndoVO) arg);
    }

    /**
     * 方法：undoValue
     * 描述：填充值
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param vo :
     * @return : void
     * @date: 2020年12月15日 1:20 下午
     */
    public abstract void undoValue(com.zzb.sensitive.undo.UndoVO vo);
}
