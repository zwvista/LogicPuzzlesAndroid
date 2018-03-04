package com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing.android

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing.data.NorthPoleFishingDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing.domain.NorthPoleFishingGame
import com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing.domain.NorthPoleFishingGameMove
import com.zwstudio.logicpuzzlesandroid.puzzles.northpolefishing.domain.NorthPoleFishingGameState

import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class NorthPoleFishingMainActivity : GameMainActivity<NorthPoleFishingGame, NorthPoleFishingDocument, NorthPoleFishingGameMove, NorthPoleFishingGameState>() {
    @Bean
    protected lateinit var document: NorthPoleFishingDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        NorthPoleFishingOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc().resumeGame()
        NorthPoleFishingGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class NorthPoleFishingOptionsActivity : GameOptionsActivity<NorthPoleFishingGame, NorthPoleFishingDocument, NorthPoleFishingGameMove, NorthPoleFishingGameState>() {
    @Bean
    protected lateinit var document: NorthPoleFishingDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class NorthPoleFishingHelpActivity : GameHelpActivity<NorthPoleFishingGame, NorthPoleFishingDocument, NorthPoleFishingGameMove, NorthPoleFishingGameState>() {
    @Bean
    protected lateinit var document: NorthPoleFishingDocument
    override fun doc() = document
}
