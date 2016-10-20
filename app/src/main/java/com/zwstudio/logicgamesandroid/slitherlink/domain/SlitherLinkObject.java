package com.zwstudio.logicgamesandroid.slitherlink.domain;

/**
 * Created by TCC-2-9002 on 2016/10/20.
 */

public class SlitherLinkObject implements Cloneable {
    SlitherLinkGame.ObjectType objTypeHorz = SlitherLinkGame.ObjectType.Empty;
    SlitherLinkGame.ObjectType objTypeVert = SlitherLinkGame.ObjectType.Empty;

    @Override
    public SlitherLinkObject clone(){
        try {
            SlitherLinkObject o = (SlitherLinkObject)super.clone();
            o.objTypeHorz = objTypeHorz;
            o.objTypeVert = objTypeVert;
            return o;
        } catch(CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
}
