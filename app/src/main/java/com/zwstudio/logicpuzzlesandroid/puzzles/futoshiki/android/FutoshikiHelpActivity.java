package com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.data.FutoshikiDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.futoshiki.domain.FutoshikiGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_help)
public class FutoshikiHelpActivity extends GameHelpActivity<FutoshikiGame, FutoshikiDocument, FutoshikiGameMove, FutoshikiGameState> {
    @Bean
    protected FutoshikiDocument document;
    public FutoshikiDocument doc() {return document;}
}
