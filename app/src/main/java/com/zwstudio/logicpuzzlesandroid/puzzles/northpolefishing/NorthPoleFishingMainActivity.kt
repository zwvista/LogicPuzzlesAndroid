package com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class NorthPoleFishingMainActivity : GameMainActivity<NorthPoleFishingGame, NorthPoleFishingDocument, NorthPoleFishingGameMove, NorthPoleFishingGameState>() {
    @Bean
    protected lateinit var document: NorthPoleFishingDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        NorthPoleFishingOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        NorthPoleFishingGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class NorthPoleFishingOptionsActivity : GameOptionsActivity<NorthPoleFishingGame, NorthPoleFishingDocument, NorthPoleFishingGameMove, NorthPoleFishingGameState>() {
    @Bean
    protected lateinit var document: NorthPoleFishingDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class NorthPoleFishingHelpActivity : GameHelpActivity<NorthPoleFishingGame, NorthPoleFishingDocument, NorthPoleFishingGameMove, NorthPoleFishingGameState>() {
    @Bean
    protected lateinit var document: NorthPoleFishingDocument
    override val doc get() = document
}
