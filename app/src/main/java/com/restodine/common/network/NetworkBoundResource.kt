package com.restodine.common.network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.restodine.App
import com.restodine.common.utils.AppUtils


abstract class NetworkBoundResource<ResultType, NetworkRequestType> @MainThread
constructor() {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        val dbSource = this.loadFromDb()
        result.addSource(dbSource) { resultType ->
            result.removeSource(dbSource)
            val hasNetwork = AppUtils.isConnectingToInternet(App.app)
            if (hasNetwork && shouldFetch(resultType)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { rT -> result.value = Resource.success(rT, Source.LOCAL) }
            }
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { resultType ->
            result.value = Resource.loading(resultType, Source.LOCAL)
        }

        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            if (response!!.isSuccessful) {
                ioThread {
                    processResponse(response)?.let { saveCallResult(it) }
                    mainThread {
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from network.
                        result.addSource(loadFromDb()) { resultType ->
                            result.value = Resource.success(resultType, Source.NETWORK)
                        }
                    }
                }
            } else {
                onFetchFailed()
                result.addSource(dbSource) { resultType ->
                    result.value = Resource.error(response.error, resultType)
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @WorkerThread
    private fun processResponse(response: ApiResponse<NetworkRequestType>): NetworkRequestType? {
        return response.body
    }

    @WorkerThread
    protected open fun saveCallResult(item: NetworkRequestType) {

    }

    @MainThread
    protected open fun shouldFetch(data: ResultType?): Boolean {
        return true
    }

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<NetworkRequestType>>

}