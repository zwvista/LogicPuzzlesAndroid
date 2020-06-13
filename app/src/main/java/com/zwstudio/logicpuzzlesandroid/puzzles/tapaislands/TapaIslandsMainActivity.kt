package com.zwstudio.logicpuzzlesandroid.puzzles.tapaislands

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class TapaIslandsMainActivity : GameMainActivity<TapaIslandsGame, TapaIslandsDocument, TapaIslandsGameMove, TapaIslandsGameState>() {
    @Bean
    protected lateinit var document: TapaIslandsDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        TapaIslandsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        TapaIslandsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TapaIslandsOptionsActivity : GameOptionsActivity<TapaIslandsGame, TapaIslandsDocument, TapaIslandsGameMove, TapaIslandsGameState>() {
    @Bean
    protected lateinit var document: TapaIslandsDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class TapaIslandsHelpActivity : GameHelpActivity<TapaIslandsGame, TapaIslandsDocument, TapaIslandsGameMove, TapaIslandsGameState>() {
    @Bean
    protected lateinit var document: TapaIslandsDocument
    override val doc get() = document
}