package com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.data.BoxItUpDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.boxitup.domain.BoxItUpGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class BoxItUpHelpActivity extends GameHelpActivity<BoxItUpGame, BoxItUpDocument, BoxItUpGameMove, BoxItUpGameState> {
    @Bean
    protected BoxItUpDocument document;
    public BoxItUpDocument doc() {return document;}
}
