package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.data.MagnetsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain.MagnetsGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class MagnetsHelpActivity extends GameHelpActivity<MagnetsGame, MagnetsDocument, MagnetsGameMove, MagnetsGameState> {
    public MagnetsDocument doc() {return app.magnetsDocument;}
}
