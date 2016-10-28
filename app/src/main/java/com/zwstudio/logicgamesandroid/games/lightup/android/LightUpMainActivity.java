package com.zwstudio.logicgamesandroid.games.lightup.android;

import com.zwstudio.logicgamesandroid.R;
import com.zwstudio.logicgamesandroid.common.android.MainActivity;
import com.zwstudio.logicgamesandroid.games.lightup.data.LightUpDocument;
import com.zwstudio.logicgamesandroid.games.lightup.domain.LightUpGame;
import com.zwstudio.logicgamesandroid.games.lightup.domain.LightUpGameMove;
import com.zwstudio.logicgamesandroid.games.lightup.domain.LightUpGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_lightup_main)
public class LightUpMainActivity extends MainActivity<LightUpGame, LightUpDocument, LightUpGameMove, LightUpGameState> {
    public LightUpDocument doc() {return app.lightUpDocument;}

    @AfterViews
    void init() {
        int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 16, 24, 34, 81};
        super.init(levels);
    }

    @Click
    void btnOptions() {
        LightUpOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        LightUpGameActivity_.intent(this).start();
    }
}
