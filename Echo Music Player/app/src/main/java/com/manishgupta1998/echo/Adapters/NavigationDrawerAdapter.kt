package com.manishgupta1998.echo.Adapters

import android.content.Context
import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.manishgupta1998.echo.Activities.MainActivity
import com.manishgupta1998.echo.Fragments.AboutUsFragment
import com.manishgupta1998.echo.Fragments.FavoriteFragment
import com.manishgupta1998.echo.Fragments.MainScreenFragment
import com.manishgupta1998.echo.Fragments.SettingsFragment
import com.manishgupta1998.echo.R

/**
 * Created by Manish Gupta on 18-01-2018.
 */

class NavigationDrawerAdapter(_contentList : ArrayList<String>,_getImages : IntArray, _context : Context)
    : RecyclerView.Adapter<NavigationDrawerAdapter.NavViewHolder>() {
    var contentList : ArrayList<String> ?= null
    var getImages : IntArray ?= null
    var mcontext : Context ?= null
    init{
        this.contentList = _contentList
        this.getImages = _getImages
        this.mcontext = _context
    }
    override fun onBindViewHolder(holder: NavViewHolder?, position: Int) {
        holder?.icon_GET?.setBackgroundResource(getImages?.get(position) as Int)
        holder?.text_GET?.setText(contentList?.get(position))
        holder?.content_Holder?.setOnClickListener({
            if(position == 0){
                val mainScreenFragment = MainScreenFragment()
                (mcontext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment, mainScreenFragment)
                        .commit()
            }else if(position == 1){
                val favoriteFragment = FavoriteFragment()
                (mcontext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment, favoriteFragment)
                        .addToBackStack("MainScreenFragment")
                        .commit()
            }else if(position == 2){
                val settingsFragment = SettingsFragment()
                (mcontext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment, settingsFragment)
                        .addToBackStack("MainScreenFragment")
                        .commit()
            }else{
                val aboutUsFragment = AboutUsFragment()
                (mcontext as MainActivity).supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.details_fragment, aboutUsFragment)
                        .addToBackStack("MainScreenFragment")
                        .commit()
            }
            MainActivity.Statified.drawerLayout?.closeDrawers()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NavViewHolder {
       var itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.row_custom_navigationdrawer, parent, false)
        return NavViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        if(contentList==null){
            return 0
        }
        else{
            return (contentList as ArrayList<String>).size as Int
        }
    }

    class NavViewHolder(view: View?): RecyclerView.ViewHolder(view){

        var icon_GET : ImageView ?=null
        var text_GET : TextView ?=null
        var content_Holder : RelativeLayout ?=null
        init{
            icon_GET = view?.findViewById(R.id.icon_navdrawer)
            text_GET = view?.findViewById(R.id.text_navdrawer)
            content_Holder = view?.findViewById(R.id.navdrawer_item_content_holder)

        }

    }
}