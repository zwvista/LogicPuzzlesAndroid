package com.zwstudio.logicpuzzlesandroid.puzzles.rooms

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class RoomsMainActivity : GameMainActivity<RoomsGame, RoomsDocument, RoomsGameMove, RoomsGameState>() {
    @Bean
    protected lateinit var document: RoomsDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        RoomsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc.resumeGame()
        RoomsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class RoomsOptionsActivity : GameOptionsActivity<RoomsGame, RoomsDocument, RoomsGameMove, RoomsGameState>() {
    @Bean
    protected lateinit var document: RoomsDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class RoomsHelpActivity : GameHelpActivity<RoomsGame, RoomsDocument, RoomsGameMove, RoomsGameState>() {
    @Bean
    protected lateinit var document: RoomsDocument
    override val doc get() = document
}