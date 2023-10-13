package com.dial.plinko.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dial.plinko.model.InfoUrl
import com.dial.plinko.api.ApiInterface

class InfoUrlRepository(private val apiInterface: ApiInterface) {
    private val infoIPLiveData = MutableLiveData<InfoUrl>()

    val urlInfo: LiveData<InfoUrl>
        get() = infoIPLiveData

    suspend fun getInfoUrl(phoneName: String, locale: String, unique: String) {
        val result = apiInterface.setInfoUser(phoneName, locale, unique)
        if(result.body() != null) {
            infoIPLiveData.postValue(result.body())
        }
    }
}