package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameGameActivity
import com.zwstudio.logicpuzzlesandroid.common.data.GameLevel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_game)
class HolidayIslandGameActivity : GameGameActivity<HolidayIslandGame, HolidayIslandDocument, HolidayIslandGameMove, HolidayIslandGameState>() {
    @Bean
    protected lateinit var document: HolidayIslandDocument
    override val doc get() = document

    @AfterViews
    override fun init() {
        gameView = HolidayIslandGameView(this)
        super.init()
    }

    override fun newGame(level: GameLevel) =
        HolidayIslandGame(level.layout, this, doc)

    @Click
    protected fun btnHelp() {
        HolidayIslandHelpActivity_.intent(this).start()
    }
}