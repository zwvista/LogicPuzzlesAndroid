package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data.MasyuDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class MasyuHelpActivity extends GameHelpActivity<MasyuGame, MasyuDocument, MasyuGameMove, MasyuGameState> {
    public MasyuDocument doc() {return app.masyuDocument;}

    @AfterViews
    protected void init() {
        super.init();
    }
}
