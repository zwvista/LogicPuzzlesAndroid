package com.zwstudio.logicgamesandroid.games.hitori.android;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.common.android.MainActivity;
import com.zwstudio.logicgamesandroid.games.hitori.data.HitoriDocument;
import com.zwstudio.logicgamesandroid.games.hitori.domain.HitoriGame;
import com.zwstudio.logicgamesandroid.games.hitori.domain.HitoriGameMove;
import com.zwstudio.logicgamesandroid.games.hitori.domain.HitoriGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_hitori_main)
public class HitoriMainActivity extends MainActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState> {
    public HitoriDocument doc() {return app.hitoriDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        HitoriOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        HitoriGameActivity_.intent(this).start();
    }
}
