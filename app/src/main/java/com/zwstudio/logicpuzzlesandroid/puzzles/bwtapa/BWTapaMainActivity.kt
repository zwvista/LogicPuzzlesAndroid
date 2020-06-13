package com.zwstudio.logicpuzzlesandroid.puzzles.bwtapa

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class BWTapaMainActivity : GameMainActivity<BWTapaGame, BWTapaDocument, BWTapaGameMove, BWTapaGameState>() {
    @Bean
    protected lateinit var document: BWTapaDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        BWTapaOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        BWTapaGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class BWTapaOptionsActivity : GameOptionsActivity<BWTapaGame, BWTapaDocument, BWTapaGameMove, BWTapaGameState>() {
    @Bean
    protected lateinit var document: BWTapaDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class BWTapaHelpActivity : GameHelpActivity<BWTapaGame, BWTapaDocument, BWTapaGameMove, BWTapaGameState>() {
    @Bean
    protected lateinit var document: BWTapaDocument
    override val doc get() = document
}