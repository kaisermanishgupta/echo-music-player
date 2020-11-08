package com.manishgupta1998.echo.Adapters

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.manishgupta1998.echo.Fragments.SongPlayingFragment
import com.manishgupta1998.echo.R
import com.manishgupta1998.echo.Songs

/**
 * Created by Manish Gupta on 25-01-2018.
 */
class FavoriteAdapter(_songDetails : ArrayList<Songs>, _context : Context) :
RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>(){


    var songDetails : ArrayList<Songs> ?= null
    var mcontext : Context?= null
    init{
        songDetails = _songDetails
        mcontext = _context
    }


    override fun getItemCount(): Int {
        if(songDetails==null){
            return 0
        }
        else{
            return (songDetails as ArrayList<Songs>).size as Int
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val songObject = songDetails?.get(position)
        holder?.trackTitle?.text = songObject?.songTitle
        holder?.trackArtist?.text = songObject?.artist
        holder?.contentHolder?.setOnClickListener({
            val songPlayingFragment = SongPlayingFragment()
            var args = Bundle()
            args.putString("songArtist", songObject?.artist)
            args.putString("path", songObject?.songData)
            args.putString("songTitle", songObject?.songTitle)
            args.putInt("songID", songObject?.songID?.toInt() as Int)
            args.putInt("songPosition", position)
            args.putParcelableArrayList("songData", songDetails)
            songPlayingFragment.arguments = args
            (mcontext as FragmentActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.details_fragment, songPlayingFragment)
                    .addToBackStack("SongPlayingFragmentFavorite")
                    .commit()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        var itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.row_custom_mainscreen_adapter, parent, false)
        return FavoriteAdapter.MyViewHolder(itemView)

    }

    class MyViewHolder(view: View?): RecyclerView.ViewHolder(view){
        var trackTitle : TextView?=null
        var trackArtist : TextView?=null
        var contentHolder : RelativeLayout?=null
        init{
            trackTitle = view?.findViewById<TextView>(R.id.trackTitle)
            trackArtist = view?.findViewById<TextView>(R.id.trackArtist)
            contentHolder = view?.findViewById<RelativeLayout>(R.id.contentRow)
        }
    }


}