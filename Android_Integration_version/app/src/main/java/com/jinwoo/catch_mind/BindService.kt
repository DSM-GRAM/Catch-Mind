package com.jinwoo.catch_mind

import android.app.Service
import android.content.Intent
import android.media.browse.MediaBrowser
import android.os.Binder
import android.os.IBinder
import android.telecom.InCallService
import android.util.Log

class BindService : Service() {

    private val mBinder = BindServiceBinder()
    private var mCallback: ICallback? = null

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    // declare callback function
    interface ICallback {
        fun remoteCall()
    }

    // for registration in activity
    fun registerCallback(cb: ICallback) {
        mCallback = cb
    }

    // service contents
    fun myServiceFunc() {
        Log.d("BindService", "called by Activity")

        // call callback in Activity
        mCallback!!.remoteCall()
    }

    // Declare inner class
    inner class BindServiceBinder : Binder() {
        val service: BindService
            get() = this@BindService
    }
}