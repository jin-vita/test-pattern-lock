package com.jinvita.testpatternlock

interface DataListener {
    fun onDataReceived(value: String, type: String = "")
}