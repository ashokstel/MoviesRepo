package com.restodine.common.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread


abstract class NetworkOnlyResource<ResultType> @MainThread constructor() {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        fetchFromNetwork()
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall()

        // Posting loading event with empty data
        result.postValue(Resource.loading(null, Source.UNKNOWN))

        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)

            if (response!!.isSuccessful) {
                ioThread {
                    processResponse(response)?.let { saveCallResult(it) }
                    mainThread {
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from network.
                        result.value = Resource.success(response.body, Source.NETWORK)
                    }
                }
            } else {
                onFetchFailed()
                result.value = Resource.error(response.error, response.body)
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @WorkerThread
    private fun processResponse(response: ApiResponse<ResultType>): ResultType? {
        return response.body
    }

    @WorkerThread
    protected open fun saveCallResult(item: ResultType) {

    }

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<ResultType>>

}