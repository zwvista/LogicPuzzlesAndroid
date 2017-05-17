package com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.data.LightenUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.lightenup.domain.LightenUpGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class LightenUpHelpActivity extends GameHelpActivity<LightenUpGame, LightenUpDocument, LightenUpGameMove, LightenUpGameState> {
    public LightenUpDocument doc() {return app.lightenupDocument;}
}
