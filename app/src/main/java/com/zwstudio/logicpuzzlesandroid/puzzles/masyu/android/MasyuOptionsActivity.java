package com.zwstudio.logicpuzzlesandroid.puzzles.masyu.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.data.MasyuDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.masyu.domain.MasyuGameState;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_options)
public class MasyuOptionsActivity extends GameOptionsActivity<MasyuGame, MasyuDocument, MasyuGameMove, MasyuGameState> {
    public MasyuDocument doc() {return app.masyuDocument;}

    protected void onDefault() {}
}
