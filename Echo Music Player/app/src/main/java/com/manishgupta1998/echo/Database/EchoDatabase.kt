package com.manishgupta1998.echo.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.manishgupta1998.echo.Songs

/**
 * Created by Manish Gupta on 25-01-2018.
 */
class EchoDatabase: SQLiteOpenHelper {

    var _songList = ArrayList<Songs>()


    object Staticated{
        var db_version = 1
        val db_name = "FavoriteDatabase"
        val table_name = "FavoriteTable"
        val column_id = "SongID"
        val column_song_title = "SongTitle"
        val column_song_artist = "SongArtist"
        val column_song_path = "SongPath"
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        sqLiteDatabase?.execSQL("CREATE TABLE "+Staticated.table_name+"("+Staticated.column_id+" INTEGER,"+Staticated.column_song_artist+" TEXT,"+Staticated.column_song_title+" TEXT,"+Staticated.column_song_path+" TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(context, name, factory,version)

    constructor(context: Context?) : super(context, Staticated.db_name, null, Staticated.db_version)

    fun storeAsFavorite(id:Int?, artist:String?, songTitle:String?, path:String?){
        val db = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put(Staticated.column_id, id)
        contentValues.put(Staticated.column_song_artist, artist)
        contentValues.put(Staticated.column_song_title, songTitle)
        contentValues.put(Staticated.column_song_path, path)
        db.insert(Staticated.table_name,null,contentValues)
        db.close()
    }

    fun queryDBList() : ArrayList<Songs>?{
        try{
            val db = this.readableDatabase
            val query_params = "SELECT * FROM "+Staticated.table_name
            var cSor = db.rawQuery(query_params, null)
            if(cSor.moveToFirst()){
                do{
                    var _id = cSor.getInt(cSor.getColumnIndexOrThrow(Staticated.column_id))
                    var _title = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.column_song_title))
                    var _artist = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.column_song_artist))
                    var _path = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.column_song_path))
                    _songList.add(Songs(_id.toLong(), _title, _artist, _path,0))
                }while(cSor.moveToNext())
            }
            else{
                return null
            }

        }catch(e:Exception){
            e.printStackTrace()
        }
        return _songList
    }

    fun checkifIdExists(_id : Int):Boolean{

        var storeID = -1090
        val db = this.readableDatabase
        val query_params = "SELECT * FROM "+Staticated.table_name+" WHERE SongID = '$_id'"
        var cSor = db.rawQuery(query_params, null)
        if(cSor.moveToFirst()){
            do {
                storeID = cSor.getInt(cSor.getColumnIndexOrThrow(Staticated.column_id))
            }while(cSor.moveToNext())
        }else{
            return false
        }
        return storeID != -1090
    }

    fun deleteFavorite(_id:Int){
        val db = this.writableDatabase
        db.delete(Staticated.table_name, Staticated.column_id + "=" + _id, null)
        db.close()
    }

    fun checkSize() : Int {
        var counter = 0
        val db = this.readableDatabase
        val query_params = "SELECT * FROM "+Staticated.table_name
        var cSor = db.rawQuery(query_params, null)
        if(cSor.moveToFirst()){
            do {
               counter += 1
            }while(cSor.moveToNext())
        }else{
            counter = 0
        }
        return counter
    }

}