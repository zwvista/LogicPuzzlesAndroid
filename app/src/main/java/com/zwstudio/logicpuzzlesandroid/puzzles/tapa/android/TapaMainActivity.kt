package com.zwstudio.logicpuzzlesandroid.puzzles.tapa.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tapa.domain.TapaGame
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class TapaMainActivity : GameMainActivity<TapaGame, TapaDocument, TapaGameMove, TapaGameState>() {
    @Bean
    protected lateinit var document: TapaDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        TapaOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        TapaGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TapaOptionsActivity : GameOptionsActivity<TapaGame, TapaDocument, TapaGameMove, TapaGameState>() {
    @Bean
    protected lateinit var document: TapaDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class TapaHelpActivity : GameHelpActivity<TapaGame, TapaDocument, TapaGameMove, TapaGameState>() {
    @Bean
    protected lateinit var document: TapaDocument
    override fun doc() = document
}