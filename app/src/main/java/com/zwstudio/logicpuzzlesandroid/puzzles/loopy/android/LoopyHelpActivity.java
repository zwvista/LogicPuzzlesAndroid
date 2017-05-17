package com.zwstudio.logicpuzzlesandroid.puzzles.loopy.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.data.LoopyDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.loopy.domain.LoopyGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class LoopyHelpActivity extends GameHelpActivity<LoopyGame, LoopyDocument, LoopyGameMove, LoopyGameState> {
    public LoopyDocument doc() {return app.loopyDocument;}
}
