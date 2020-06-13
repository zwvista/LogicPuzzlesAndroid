package com.zwstudio.logicpuzzlesandroid.home.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
class DBHelper(context: Context) : OrmLiteSqliteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    /*
    * Check whether or not database exist
    */
    private fun checkdatabase(): Boolean {
        val checkdb: Boolean
        val myPath = databasePath + DATABASE_NAME
        val dbfile = File(myPath)
        checkdb = dbfile.exists()
        Log.i(DBHelper::class.java.name, "DB Exist : $checkdb")
        return checkdb
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    override fun onCreate(db: SQLiteDatabase, connectionSource: ConnectionSource) {}

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    override fun onUpgrade(db: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {}

    /**
     * Close the database connections and clear any cached DAOs.
     */
    override fun close() {
        super.close()
    }

    companion object {
        private const val DATABASE_NAME = "Game.db"
        private lateinit var databasePath: String
        private const val DATABASE_VERSION = 1
    }

    init {
        databasePath = context.getDatabasePath(DATABASE_NAME).parent + "/"
        val dbexist = checkdatabase()
        if (!dbexist) {
            // If database did not exist, try copying existing database from assets folder.
            try {
                val dir = File(databasePath)
                dir.mkdirs()
                val myinput = context.assets.open(DATABASE_NAME)
                val outfilename = databasePath + DATABASE_NAME
                Log.i(DBHelper::class.java.name, "DB Path : $outfilename")
                val myoutput = FileOutputStream(outfilename)
                val buffer = ByteArray(1024)
                var length: Int
                while (myinput.read(buffer).also { length = it } > 0) {
                    myoutput.write(buffer, 0, length)
                }
                myoutput.flush()
                myoutput.close()
                myinput.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}