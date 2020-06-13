package com.zwstudio.logicpuzzlesandroid.puzzles.holidayisland

import com.zwstudio.logicpuzzlesandroid.R
import com.zwstudio.logicpuzzlesandroid.common.android.GameHelpActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameMainActivity
import com.zwstudio.logicpuzzlesandroid.common.android.GameOptionsActivity
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_game_main)
class HolidayIslandMainActivity : GameMainActivity<HolidayIslandGame, HolidayIslandDocument, HolidayIslandGameMove, HolidayIslandGameState>() {
    @Bean
    protected lateinit var document: HolidayIslandDocument
    override val doc get() = document

    @Click
    fun btnOptions() {
        HolidayIslandOptionsActivity_.intent(this).start()
    }

    override fun resumeGame() {
        doc.resumeGame()
        HolidayIslandGameActivity_.intent(this).start()
    }
}

@EActivity(R.layout.activity_game_options)
class HolidayIslandOptionsActivity : GameOptionsActivity<HolidayIslandGame, HolidayIslandDocument, HolidayIslandGameMove, HolidayIslandGameState>() {
    @Bean
    protected lateinit var document: HolidayIslandDocument
    override val doc get() = document
}

@EActivity(R.layout.activity_game_help)
class HolidayIslandHelpActivity : GameHelpActivity<HolidayIslandGame, HolidayIslandDocument, HolidayIslandGameMove, HolidayIslandGameState>() {
    @Bean
    protected lateinit var document: HolidayIslandDocument
    override val doc get() = document
}