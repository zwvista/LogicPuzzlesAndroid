package com.zwstudio.logicpuzzlesandroid.puzzles.kropki.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.data.KropkiDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.kropki.domain.KropkiGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class KropkiHelpActivity extends GameHelpActivity<KropkiGame, KropkiDocument, KropkiGameMove, KropkiGameState> {
    @Bean
    protected KropkiDocument document;
    public KropkiDocument doc() {return document;}
}
