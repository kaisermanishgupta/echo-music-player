package com.manishgupta1998.echo.Fragments


import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.manishgupta1998.echo.Adapters.FavoriteAdapter
import com.manishgupta1998.echo.Database.EchoDatabase
import com.manishgupta1998.echo.R
import com.manishgupta1998.echo.Songs
import kotlinx.android.synthetic.main.fragment_favorite.*


/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragment : Fragment() {

    var myActivity : Activity ?=null
    var noFavorites : TextView?=null
    var recyclerView : RecyclerView?=null
    var nowPlayingBottomBar : RelativeLayout ?= null
    var playPauseButton : ImageButton?=null
    var songTitle : TextView?=null
    var trackPositon : Int = 0
    var favContent : EchoDatabase ?=null

    var refreshList : ArrayList<Songs> ?= null
    var getListfromDB : ArrayList<Songs> ?= null

    object Statified{
        var mediaplayer : MediaPlayer ?= null
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fview = inflater!!.inflate(R.layout.fragment_favorite, container, false)
        noFavorites = fview?.findViewById(R.id.noFavorites)
        nowPlayingBottomBar = fview.findViewById(R.id.hiddenBarFavScreen)
        setHasOptionsMenu(true)
        songTitle = fview.findViewById(R.id.songTitle)
        activity.title = "Favorites"
        playPauseButton = fview.findViewById(R.id.playPauseButton)
        recyclerView = fview.findViewById(R.id.favoriteRecycler)
        return fview
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myActivity = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favContent = EchoDatabase(myActivity)
        display_favorites_by_searching()
        bottomBarSetup()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }


    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        var item = menu?.findItem(R.id.action_sort)
        item?.isVisible = false
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun getSongsFromPhone() : ArrayList<Songs>{
        var arrayList = ArrayList<Songs>()
        var contentResolver = myActivity?.contentResolver
        var songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var songCursor = contentResolver?.query(songUri,null,null,null,null,null)
        if(songCursor!=null && songCursor.moveToFirst()){
            val songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val dateIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            while(songCursor.moveToNext()){
                var currentID = songCursor.getLong(songId)
                var currentTitle = songCursor.getString(songTitle)
                var currentArist = songCursor.getString(songArtist)
                var currentData = songCursor.getString(songData)
                var currentDate = songCursor.getLong(dateIndex)
                arrayList.add(Songs(currentID, currentTitle,currentArist,currentData,currentDate))
            }
        }
        return arrayList
    }


    fun bottomBarSetup(){
        try{
            bottomBarClickHandler()
            songTitle?.setText(SongPlayingFragment.Statified.currentSongHelper?.songTitle)
            SongPlayingFragment.Statified.mediaPlayer?.setOnCompletionListener({
                songTitle?.setText(SongPlayingFragment.Statified.currentSongHelper?.songTitle)
                SongPlayingFragment.Staticated.onSongComplete()
            })
            if(SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                nowPlayingBottomBar?.visibility = View.VISIBLE
            }else{
                nowPlayingBottomBar?.visibility = View.INVISIBLE
            }

        }catch(e : Exception){
            e.printStackTrace()
        }
    }

    fun bottomBarClickHandler(){
        nowPlayingBottomBar?.setOnClickListener({
            Statified.mediaplayer = SongPlayingFragment.Statified.mediaPlayer
            val songPlayingFragment = SongPlayingFragment()
            var args = Bundle()
            args.putString("songArtist", SongPlayingFragment.Statified.currentSongHelper?.songArtist)
            args.putString("path", SongPlayingFragment.Statified.currentSongHelper?.songPath)
            args.putString("songTitle", SongPlayingFragment.Statified.currentSongHelper?.songTitle)
            args.putInt("songID", SongPlayingFragment.Statified.currentSongHelper?.songID?.toInt() as Int)
            args.putInt("songPosition", SongPlayingFragment.Statified.currentSongHelper?.currentPosition as Int)
            args.putParcelableArrayList("songData", SongPlayingFragment.Statified.fetchSongs)
            args.putString("FavBottomBar", "Success")
            songPlayingFragment.arguments = args
            fragmentManager.beginTransaction()
                    .replace(R.id.details_fragment, songPlayingFragment)
                    .addToBackStack("SongPlayingFragment")
                    .commit()
        })

        playPauseButton?.setOnClickListener({

            if(SongPlayingFragment.Statified.mediaPlayer?.isPlaying as Boolean){
                SongPlayingFragment.Statified.mediaPlayer?.pause()
                trackPositon = SongPlayingFragment.Statified.mediaPlayer?.currentPosition as Int
                playPauseButton?.setBackgroundResource(R.drawable.play_icon)
            }else{
                SongPlayingFragment.Statified.mediaPlayer?.seekTo(trackPositon)
                SongPlayingFragment.Statified.mediaPlayer?.start()
                SongPlayingFragment.Statified.currentSongHelper?.isPlaying = true
                playPauseButton?.setBackgroundResource(R.drawable.pause_icon)
            }
        })
    }

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun display_favorites_by_searching(){
        if(favContent?.checkSize() as Int > 0){
            refreshList = ArrayList<Songs>()
            getListfromDB = favContent?.queryDBList()
            var fetchListFromDevice = getSongsFromPhone()
            if(fetchListFromDevice!=null){
                for(i in 0..fetchListFromDevice.size-1){
                    for(j in 0..getListfromDB?.size as Int - 1){

                        if((getListfromDB?.get(j)?.songID == (fetchListFromDevice.get(i).songID))){
                            refreshList?.add((getListfromDB as ArrayList<Songs>).get(j))
                        }
                    }
                }
            }

            if(refreshList==null){
                recyclerView?.visibility = View.INVISIBLE
                noFavorites?.visibility = View.VISIBLE
            }else{
                var favoriteAdapter = FavoriteAdapter(refreshList as ArrayList<Songs>, myActivity as Context)
                val mlayoutManager = LinearLayoutManager(activity)
                recyclerView?.layoutManager = mlayoutManager
                recyclerView?.itemAnimator = DefaultItemAnimator()
                recyclerView?.adapter = favoriteAdapter
                recyclerView?.setHasFixedSize(true)
            }
        }
    else{
            recyclerView?.visibility = View.INVISIBLE
            noFavorites?.visibility = View.VISIBLE
        }

}

}// Required empty public constructor
