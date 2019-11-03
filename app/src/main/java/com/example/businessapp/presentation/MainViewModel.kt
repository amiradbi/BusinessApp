package com.example.businessapp.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.businessapp.application.extensions.SingleLiveEvent
import com.example.businessapp.domain.BusinessRepository
import com.example.businessapp.domain.model.Business
import io.reactivex.disposables.Disposable
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class MainViewModel @Inject constructor(private val businessRepository: BusinessRepository) :
    ViewModel() {

    private companion object {
        private const val MAP_QUERY = "http://maps.google.com?q="

        private const val MELBOURNE = "Melbourne"
        private const val CATEGORY = "coffee"
        private const val LIMIT = 15
    }

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private val _navigation = SingleLiveEvent<NavigationEvent>()
    val navigation: LiveData<NavigationEvent> = _navigation

    private val _loading = SingleLiveEvent<Loading>()
    val loading: LiveData<Loading> = _loading

    private val _message = SingleLiveEvent<String>()
    val message: LiveData<String> = _message

    private var initialised = false
    private var contentLoaded = false
    private var disposable: Disposable? = null

    fun init() {
        if (!initialised) {
            initialised = true
            fetchList()
        }
    }

    override fun onCleared() {
        disposable?.dispose()
        disposable = null
        super.onCleared()
    }

    fun onViewMapClicked(lat: Double, long: Double) {
        _navigation.value = NavigationEvent("$MAP_QUERY$lat,$long")
    }

    fun onRefreshInvoked() {
        fetchList()
    }

    private fun fetchList() {
        disposable = businessRepository.getBusinesses(
            location = MELBOURNE,
            limit = LIMIT,
            category = CATEGORY
        )
            .doOnSubscribe {
                _loading.postValue(Loading.ContentLoading(!contentLoaded))
            }
            .doAfterTerminate {
                _loading.value = (Loading.HideSweepRefresh)
                _loading.value = (Loading.ContentLoading(false))
            }
            .subscribe(::onSuccess, ::onFailure)
    }

    private fun onSuccess(businesses: List<Business>) {
        contentLoaded = true
        _viewState.postValue(ViewState(businesses.sortedByDescending { it.reviewCount }))
    }

    private fun onFailure(it: Throwable) {
        Log.e("MAIN VIEW MODEL", it.message.orEmpty())
        _message.postValue(resolveError(it))
        //TODO: good to have a nicer way for failure
    }

    private fun resolveError(error: Throwable) =
        when (error) {
            is SocketException,
            is UnknownHostException -> "No Network Error"
            is SocketTimeoutException -> "Timeout Error"
            else -> "Something went wrong"
        }

    data class ViewState(val businesses: List<Business>)

    data class NavigationEvent(val query: String)

    sealed class Loading {
        object HideSweepRefresh : Loading()
        class ContentLoading(val isVisible: Boolean) : Loading()
    }
}