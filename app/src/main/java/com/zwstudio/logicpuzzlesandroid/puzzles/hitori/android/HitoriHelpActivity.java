package com.zwstudio.logicpuzzlesandroid.puzzles.hitori.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.data.HitoriDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.hitori.domain.HitoriGameState;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class HitoriHelpActivity extends GameHelpActivity<HitoriGame, HitoriDocument, HitoriGameMove, HitoriGameState> {
    public HitoriDocument doc() {return app.hitoriDocument;}
}
