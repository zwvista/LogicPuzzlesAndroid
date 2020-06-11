package com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.android.PairakabeGameActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.android.PairakabeOptionsActivity_
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.data.PairakabeDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGame
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.pairakabe.domain.PairakabeGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class PairakabeMainActivity : GameMainActivity<PairakabeGame, PairakabeDocument, PairakabeGameMove, PairakabeGameState>() {
    @Bean
    protected lateinit var document: PairakabeDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        PairakabeOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        PairakabeGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class PairakabeOptionsActivity : GameOptionsActivity<PairakabeGame, PairakabeDocument, PairakabeGameMove, PairakabeGameState>() {
    @Bean
    protected lateinit var document: PairakabeDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class PairakabeHelpActivity : GameHelpActivity<PairakabeGame, PairakabeDocument, PairakabeGameMove, PairakabeGameState>() {
    @Bean
    protected lateinit var document: PairakabeDocument
    override fun doc() = document
}