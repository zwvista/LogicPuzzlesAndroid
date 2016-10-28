package com.zwstudio.logicgamesandroid.slitherlink.android;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.common.android.MainActivity;
import com.zwstudio.logicgamesandroid.slitherlink.data.SlitherLinkDocument;
import com.zwstudio.logicgamesandroid.slitherlink.domain.SlitherLinkGame;
import com.zwstudio.logicgamesandroid.slitherlink.domain.SlitherLinkGameMove;
import com.zwstudio.logicgamesandroid.slitherlink.domain.SlitherLinkGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_slitherlink_main)
public class SlitherLinkMainActivity extends MainActivity<SlitherLinkGame, SlitherLinkDocument, SlitherLinkGameMove, SlitherLinkGameState> {
    public SlitherLinkDocument doc() {return app.slitherlinkDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        SlitherLinkOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        SlitherLinkGameActivity_.intent(this).start();
    }
}
