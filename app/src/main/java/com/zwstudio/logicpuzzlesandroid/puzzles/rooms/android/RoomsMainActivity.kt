package com.zwstudio.logicpuzzlesandroid.puzzles.rooms.androidimport

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.data.RoomsDocument
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGame
import com.zwstudio.logicpuzzlesandroid.puzzles.rooms.domain.RoomsGameMove
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class RoomsMainActivity : GameMainActivity<RoomsGame, RoomsDocument, RoomsGameMove, RoomsGameState>() {
    @Bean
    protected lateinit var document: RoomsDocument
    override fun doc() = document

    @Click
    fun btnOptions() {
        RoomsOptionsActivity_.intent(this).start()
    }

    protected override fun resumeGame() {
        doc().resumeGame()
        RoomsGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class RoomsOptionsActivity : GameOptionsActivity<RoomsGame, RoomsDocument, RoomsGameMove, RoomsGameState>() {
    @Bean
    protected lateinit var document: RoomsDocument
    override fun doc() = document
}

@EActivity(R.layout.activity_game_help)
class RoomsHelpActivity : GameHelpActivity<RoomsGame, RoomsDocument, RoomsGameMove, RoomsGameState>() {
    @Bean
    protected lateinit var document: RoomsDocument
    override fun doc() = document
}