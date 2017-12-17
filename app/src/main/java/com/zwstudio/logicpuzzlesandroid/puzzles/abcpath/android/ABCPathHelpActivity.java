package com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.data.ABCPathDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.abcpath.domain.ABCPathGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class ABCPathHelpActivity extends GameHelpActivity<ABCPathGame, ABCPathDocument, ABCPathGameMove, ABCPathGameState> {
    @Bean
    protected ABCPathDocument document;
    public ABCPathDocument doc() {return document;}
}
