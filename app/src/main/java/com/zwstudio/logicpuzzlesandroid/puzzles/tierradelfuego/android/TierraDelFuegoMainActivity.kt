package com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.data.TierraDelFuegoDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGame
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.tierradelfuego.domain.TierraDelFuegoGameState
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class TierraDelFuegoMainActivity : GameMainActivity<TierraDelFuegoGame, TierraDelFuegoDocument, TierraDelFuegoGameMove, TierraDelFuegoGameState>() {
    @Bean
    protected lateinit var document: TierraDelFuegoDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        TierraDelFuegoOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        TierraDelFuegoGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TierraDelFuegoOptionsActivity : GameOptionsActivity<TierraDelFuegoGame, TierraDelFuegoDocument, TierraDelFuegoGameMove, TierraDelFuegoGameState>() {
    @Bean
    protected lateinit var document: TierraDelFuegoDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class TierraDelFuegoHelpActivity : GameHelpActivity<TierraDelFuegoGame, TierraDelFuegoDocument, TierraDelFuegoGameMove, TierraDelFuegoGameState>() {
    @Bean
    protected lateinit var document: TierraDelFuegoDocument
    override val doc get() = document
}