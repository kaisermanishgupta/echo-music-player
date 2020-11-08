package com.manishgupta1998.echo.Fragments


import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import com.manishgupta1998.echo.Database.EchoDatabase
import com.manishgupta1998.echo.R


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {
    var myActivity : Activity ?= null
    var shakeSwitch : Switch ?= null

    object Statified{
        var My_Prefs_Name = "ShakeFeature"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val stview =  inflater!!.inflate(R.layout.fragment_settings, container, false)
        shakeSwitch = stview?.findViewById(R.id.switchShake)
        activity.title = "Settings"
        setHasOptionsMenu(true)
        return stview
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
        val prefs = myActivity?.getSharedPreferences(Statified.My_Prefs_Name, Context.MODE_PRIVATE)
        val isAllowed = prefs?.getBoolean("feature", false)
        if(isAllowed as Boolean){
            shakeSwitch?.isChecked = true
        }else{
            shakeSwitch?.isChecked = false
        }
        shakeSwitch?.setOnCheckedChangeListener({compoundNutton, b ->
            if(b){
                val editor = myActivity?.getSharedPreferences(Statified.My_Prefs_Name, Context.MODE_PRIVATE)?.edit()
                editor?.putBoolean("feature", true)
                editor?.apply()
            }else{

                val editor = myActivity?.getSharedPreferences(Statified.My_Prefs_Name, Context.MODE_PRIVATE)?.edit()
                editor?.putBoolean("feature", false)
                editor?.apply()
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        var item = menu?.findItem(R.id.action_sort)
        item?.isVisible = false
    }

}// Required empty public constructor
