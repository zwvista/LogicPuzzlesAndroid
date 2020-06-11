package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.data.TapaIslandsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands.domain.TapaIslandsGame
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

_
@EActivity(R.layout.activity_game_main)
class TapaIslandsMainActivity : GameMainActivity<TapaIslandsGame?, TapaIslandsDocument?, TapaIslandsGameMove?, TapaIslandsGameState?>() {
    @Bean
    protected var document: TapaIslandsDocument? = null
    override fun doc() = document

    @Click
    fun btnOptions() {
        TapaIslandsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        TapaIslandsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TapaIslandsOptionsActivity : GameOptionsActivity<TapaIslandsGame?, TapaIslandsDocument?, TapaIslandsGameMove?, TapaIslandsGameState?>() {
    @Bean
    protected var document: TapaIslandsDocument? = null
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class TapaIslandsHelpActivity : GameHelpActivity<TapaIslandsGame?, TapaIslandsDocument?, TapaIslandsGameMove?, TapaIslandsGameState?>() {
    @Bean
    protected var document: TapaIslandsDocument? = null
    override fun doc() = document
}