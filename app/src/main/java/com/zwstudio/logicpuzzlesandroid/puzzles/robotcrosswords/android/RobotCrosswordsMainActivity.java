package com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.android;

import com.zwstudio.logicpuzzlesandroid.R;
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.data.RobotCrosswordsDocument;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGame;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGameMove;
import com.zwstudio.logicpuzzlesandroid.puzzles.robotcrosswords.domain.RobotCrosswordsGameState;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_game_main)
public class RobotCrosswordsMainActivity extends GameMainActivity<RobotCrosswordsGame, RobotCrosswordsDocument, RobotCrosswordsGameMove, RobotCrosswordsGameState> {
    @Bean
    protected RobotCrosswordsDocument document;
    public RobotCrosswordsDocument doc() {return document;}

    @Click
    void btnOptions() {
        RobotCrosswordsOptionsActivity_.intent(this).start();
    }

    protected void resumeGame() {
        doc().resumeGame();
        RobotCrosswordsGameActivity_.intent(this).start();
    }
}
