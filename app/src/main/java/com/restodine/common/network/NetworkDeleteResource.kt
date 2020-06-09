package com.restodine.common.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.restodine.App
import com.restodine.common.utils.AppUtils

abstract class NetworkDeleteResource<ResultType, RequestType> @MainThread
constructor() {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        val deleteFromNetwork = this.createCall()


        val hasNetwork = AppUtils.isConnectingToInternet(App.app)


    }




    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @WorkerThread
    private fun processResponse(response: ApiResponse<RequestType>): RequestType? {
        return response.body
    }

    @WorkerThread
    protected open fun saveCallResult(item: RequestType) {

    }

    @MainThread
    protected open fun shouldFetch(data: ResultType?): Boolean {
        return true
    }

    @MainThread
    protected abstract fun onFetchFailed()

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

}
