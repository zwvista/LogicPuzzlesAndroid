package com.zwstudio.logicpuzzlesandroid.puzzles.domino.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.data.DominoDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGame
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.domino.domain.DominoGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class DominoMainActivity : GameMainActivity<DominoGame, DominoDocument, DominoGameMove, DominoGameState>() {
    @Bean
    protected lateinit var document: DominoDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        DominoOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        DominoGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class DominoOptionsActivity : GameOptionsActivity<DominoGame, DominoDocument, DominoGameMove, DominoGameState>() {
    @Bean
    protected lateinit var document: DominoDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class DominoHelpActivity : GameHelpActivity<DominoGame, DominoDocument, DominoGameMove, DominoGameState>() {
    @Bean
    protected lateinit var document: DominoDocument
    override fun doc() = document
}