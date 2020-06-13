package com.zwstudio.logicpuzzlesandroid.puzzles.tapa

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class TapaMainActivity : GameMainActivity<TapaGame, TapaDocument, TapaGameMove, TapaGameState>() {
    @Bean
    protected lateinit var document: TapaDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        TapaOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        TapaGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class TapaOptionsActivity : GameOptionsActivity<TapaGame, TapaDocument, TapaGameMove, TapaGameState>() {
    @Bean
    protected lateinit var document: TapaDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class TapaHelpActivity : GameHelpActivity<TapaGame, TapaDocument, TapaGameMove, TapaGameState>() {
    @Bean
    protected lateinit var document: TapaDocument
    override val doc get() = document
}