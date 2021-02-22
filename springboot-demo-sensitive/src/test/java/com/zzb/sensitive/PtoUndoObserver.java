package com.zzb.sensitive;

import com.zzb.sensitive.undo.UndoObserver;
import com.zzb.sensitive.undo.UndoVO;
import org.springframework.stereotype.Component;

@Component
public class PtoUndoObserver extends UndoObserver {

    /**
     * 继承观察者,可填充到方法的入参里面
     * @param vo
     */
    @Override
    public void undoValue(UndoVO vo) {
        synchronized (this) {
            if (vo.getType().equals("card")) {
                vo.undo("...1");
            }
            if (vo.getType().equals("phone")) {
                vo.undo("......2");
            }
            if (vo.getType().equals("reg")) {
                vo.undo('a');
            }
            if(vo.getType().equals("string")){
                vo.undo("............4");
            }
            if(vo.getType().equals("obj")){
                vo.undo(new SingleObj().setAuthor("............5"));
            }
        }
    }
}
