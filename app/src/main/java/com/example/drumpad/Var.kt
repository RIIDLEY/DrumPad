package com.example.drumpad

import android.R.attr.name
import android.app.Application


class Var: Application() {

        var globalVar = "I am Global Variable"
        var log: Boolean = false


    fun getLog(): Boolean? {
        return log
    }

    fun setLog(aLog: Boolean?) {
        if (aLog != null) {
            log = aLog
        }
    }

}