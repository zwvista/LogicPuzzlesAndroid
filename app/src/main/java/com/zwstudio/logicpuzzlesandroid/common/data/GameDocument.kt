package com.zwstudio.logicpuzzlesandroid.common.data

import com.zwstudio.logicpuzzlesandroid.common.domain.Game
import com.zwstudio.logicpuzzlesandroid.home.android.HomeChooseGameActivity
import com.zwstudio.logicpuzzlesandroid.home.android.LogicPuzzlesApplication
import fj.F
import fj.data.Array
import org.androidannotations.annotations.AfterInject
import org.androidannotations.annotations.App
import org.androidannotations.annotations.EBean
import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import java.sql.SQLException
import java.util.*

@EBean(scope = EBean.Scope.Singleton)
abstract class GameDocument<G : Game<*, *, *>?, GM> : GameDocumentInterface {
    fun gameID(): String {
        val name = javaClass.simpleName
        return name.substring(0, name.indexOf("Document"))
    }

    fun gameTitle(): String {
        val name = gameID()
        return ObjectUtils.defaultIfNull(HomeChooseGameActivity.Companion.name2title.get(name), name)
    }

    var levels: MutableList<GameLevel?> = ArrayList()
    var selectedLevelID: String? = null
    fun selectedLevelIDSolution(): String {
        return "$selectedLevelID Solution"
    }

    var help: List<String?> = ArrayList()

    @kotlin.jvm.JvmField
    @App
    var app: LogicPuzzlesApplication? = null

    @AfterInject
    fun init() {
        val filename = gameID() + ".xml"
        try {
            val in_s = app!!.applicationContext.assets.open("xml/$filename")
            loadXml(in_s)
            in_s.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        selectedLevelID = gameProgress()!!.levelID
    }

    private fun loadXml(in_s: InputStream) {
        val pullParserFactory: XmlPullParserFactory
        try {
            pullParserFactory = XmlPullParserFactory.newInstance()
            val parser = pullParserFactory.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(in_s, null)
            parseXML(parser)
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun parseXML(parser: XmlPullParser) {
        var eventType = parser.eventType
        val getCdata = F { strs: List<String?>? -> fj.data.List.iterableList(strs).filter { s: String? -> !StringUtils.isAllBlank(s) }.toJavaList() }
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_DOCUMENT -> {
                }
                XmlPullParser.START_TAG -> {
                    val name = parser.name
                    if (name == "level") {
                        val id = parser.getAttributeValue(null, "id")
                        val settings: MutableMap<String?, String?> = HashMap()
                        var i = 0
                        while (i < parser.attributeCount) {
                            settings[parser.getAttributeName(i)] = parser.getAttributeValue(i)
                            i++
                        }
                        var layout: List<String?> = Array.array(*parser.nextText().split("\n".toRegex()).toTypedArray())
                            .map { s: String -> s.replace("\r", "") }
                            .toJavaList()
                        layout = fj.data.List.iterableList(getCdata.f(layout))
                            .map { s: String? -> s!!.replace("`", "") }
                            .toJavaList()
                        val level = GameLevel()
                        level.id = id
                        level.layout = layout
                        level.settings = settings
                        levels.add(level)
                    } else if (name == "help") {
                        help = Array.array(*parser.nextText().split("\n".toRegex()).toTypedArray())
                            .map { s: String -> s.replace("\r", "") }
                            .toJavaList()
                        help = getCdata.f(help)
                    }
                }
                XmlPullParser.END_TAG -> {
                }
            }
            eventType = parser.next()
        }
    }

    fun gameProgress(): GameProgress? {
        return try {
            var rec = app!!.daoGameProgress!!.queryBuilder()
                .where().eq("gameID", gameID())
                .queryForFirst()
            if (rec == null) {
                rec = GameProgress()
                rec.gameID = gameID()
                rec.levelID = levels[0]!!.id!!
                app!!.daoGameProgress!!.create(rec)
            }
            rec
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    private fun levelProgress(levelID: String?): LevelProgress? {
        return try {
            var rec = app!!.daoLevelProgress!!.queryBuilder()
                .where().eq("gameID", gameID())
                .and().eq("levelID", levelID).queryForFirst()
            if (rec == null) {
                rec = LevelProgress()
                rec.gameID = gameID()
                rec.levelID = levelID
                app!!.daoLevelProgress!!.create(rec)
            }
            rec
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    fun levelProgress(): LevelProgress? {
        return levelProgress(selectedLevelID)
    }

    fun levelProgressSolution(): LevelProgress? {
        return levelProgress(selectedLevelIDSolution())
    }

    private fun moveProgress(levelID: String?): List<MoveProgress?>? {
        return try {
            val builder = app!!.daoMoveProgress!!.queryBuilder()
            builder.where().eq("gameID", gameID())
                .and().eq("levelID", levelID)
            builder.orderBy("moveIndex", true)
            builder.query()
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    fun moveProgress(): List<MoveProgress?>? {
        return moveProgress(selectedLevelID)
    }

    fun moveProgressSolution(): List<MoveProgress?>? {
        return moveProgress(selectedLevelIDSolution())
    }

    fun levelUpdated(game: Game<*, *, *>) {
        try {
            val rec = levelProgress()
            rec!!.moveIndex = game.moveIndex()
            app!!.daoLevelProgress!!.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun gameSolved(game: Game<*, *, *>) {
        val recLP = levelProgress()
        val recLPS = levelProgressSolution()
        if (recLPS!!.moveIndex == 0 || recLPS.moveIndex > recLP!!.moveIndex) saveSolution(game)
    }

    fun moveAdded(game: Game<*, *, *>, move: GM) {
        try {
            val builder = app!!.daoMoveProgress!!.deleteBuilder()
            builder.where().eq("gameID", gameID())
                .and().eq("levelID", selectedLevelID)
                .and().ge("moveIndex", game.moveIndex())
            builder.delete()
            val rec = MoveProgress()
            rec.gameID = gameID()
            rec.levelID = selectedLevelID
            rec.moveIndex = game.moveIndex()
            saveMove(move, rec)
            app!!.daoMoveProgress!!.create(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    protected abstract fun saveMove(move: GM, rec: MoveProgress?)
    abstract fun loadMove(rec: MoveProgress?): GM
    fun resumeGame() {
        try {
            val rec = gameProgress()
            rec!!.levelID = selectedLevelID!!
            app!!.daoGameProgress!!.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun clearGame() {
        try {
            val builder = app!!.daoMoveProgress!!.deleteBuilder()
            builder.where().eq("gameID", gameID())
                .and().eq("levelID", selectedLevelID)
            builder.delete()
            val rec = levelProgress()
            rec!!.moveIndex = 0
            app!!.daoLevelProgress!!.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun copyMoves(moveProgressFrom: List<MoveProgress?>?, levelIDTo: String?) {
        try {
            val builder = app!!.daoMoveProgress!!.deleteBuilder()
            builder.where().eq("gameID", gameID())
                .and().eq("levelID", levelIDTo)
            builder.delete()
            for (recMP in moveProgressFrom!!) {
                val move = loadMove(recMP)
                val recMPS = MoveProgress()
                recMPS.gameID = gameID()
                recMPS.levelID = levelIDTo
                recMPS.moveIndex = recMP!!.moveIndex
                saveMove(move, recMPS)
                app!!.daoMoveProgress!!.create(recMPS)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun saveSolution(game: Game<*, *, *>) {
        copyMoves(moveProgress(), selectedLevelIDSolution())
        try {
            val rec = levelProgressSolution()
            rec!!.moveIndex = game.moveIndex()
            app!!.daoLevelProgress!!.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun loadSolution() {
        val mps = moveProgressSolution()
        copyMoves(mps, selectedLevelID)
        try {
            val rec = levelProgress()
            rec!!.moveIndex = mps!!.size
            app!!.daoLevelProgress!!.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun deleteSolution() {
        try {
            val builder = app!!.daoMoveProgress!!.deleteBuilder()
            builder.where().eq("gameID", gameID())
                .and().eq("levelID", selectedLevelIDSolution())
            builder.delete()
            val rec = levelProgressSolution()
            rec!!.moveIndex = 0
            app!!.daoLevelProgress!!.update(rec)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun resetAllLevels() {
        try {
            val builder = app!!.daoMoveProgress!!.deleteBuilder()
            builder.where().eq("gameID", gameID())
            builder.delete()
            val builder2 = app!!.daoLevelProgress!!.deleteBuilder()
            builder2.where().eq("gameID", gameID())
            builder2.delete()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    override val markerOption: Int
        get() {
            val o = gameProgress()!!.option1
            return o?.toInt() ?: 0
        }

    override fun setMarkerOption(rec: GameProgress, o: Int) {
        rec.option1 = o.toString()
    }

    override val isAllowedObjectsOnly: Boolean
        get() {
            val o = gameProgress()!!.option2
            return java.lang.Boolean.parseBoolean(o)
        }

    override fun setAllowedObjectsOnly(rec: GameProgress, o: Boolean) {
        rec.option2 = o.toString()
    }
}