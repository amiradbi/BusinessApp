package com.example.businessapp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.example.businessapp.domain.BusinessRepository
import com.example.businessapp.domain.model.Business
import com.nhaarman.mockito_kotlin.firstValue
import com.nhaarman.mockito_kotlin.secondValue
import com.nhaarman.mockito_kotlin.thirdValue
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MainViewModelTest {

    companion object {
        private const val MELBOURNE = "Melbourne"
        private const val CATEGORY = "coffee"
        private const val LIMIT = 15
    }

    private lateinit var vm: MainViewModel

    private val response =
            listOf(
                    Business(name = "Alexander",
                            address = Business.Address(line = listOf("44 Burke st", "Melbourne"), latitude = 43.231, longitude = 56.78),
                            rating = 4.0,
                            reviewCount = 10,
                            imageUrl = "google.com"),
                    Business(name = "Sam",
                            address = Business.Address(line = listOf("30 Collins st", "Melbourne"), latitude = 56.32, longitude = 12.09),
                            rating = 3.0,
                            reviewCount = 20,
                            imageUrl = "google.com"))


    private val expectedResult =
            listOf(
                    Business(name = "Sam",
                            address = Business.Address(line = listOf("30 Collins st", "Melbourne"), latitude = 56.32, longitude = 12.09),
                            rating = 3.0,
                            reviewCount = 20,
                            imageUrl = "google.com"),
                    Business(name = "Alexander",
                            address = Business.Address(line = listOf("44 Burke st", "Melbourne"), latitude = 43.231, longitude = 56.78),
                            rating = 4.0,
                            reviewCount = 10,
                            imageUrl = "google.com"))

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    lateinit var repository: BusinessRepository

    @Mock
    lateinit var viewState: Observer<MainViewModel.ViewState>

    @Mock
    lateinit var navigationEvent: Observer<MainViewModel.NavigationEvent>

    @Mock
    lateinit var loading: Observer<MainViewModel.Loading>

    @Mock
    lateinit var message: Observer<String>

    private lateinit var lifecycle: Lifecycle

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        lifecycle = LifecycleRegistry(lifecycleOwner)
        vm = MainViewModel(repository)

        vm.viewState.observeForever(viewState)
        vm.navigation.observeForever(navigationEvent)
        vm.loading.observeForever(loading)
        vm.message.observeForever(message)

        Mockito.`when`(repository.getBusinesses(location = MELBOURNE, limit = LIMIT, category = CATEGORY))
                .thenReturn(Single.just(response))
    }

    @Test
    fun initHappyFlow() {
        val captor = ArgumentCaptor.forClass(MainViewModel.Loading::class.java)

        vm.init()

        Mockito.verify(loading, Mockito.times(3)).onChanged(captor.capture())

        // on subscribe
        kotlin.test.assertEquals(MainViewModel.Loading.ContentLoading(true).isVisible,
                (captor.firstValue as MainViewModel.Loading.ContentLoading).isVisible)

        // on success
        Mockito.verify(viewState).onChanged(MainViewModel.ViewState(expectedResult))

        // on terminate
        kotlin.test.assertEquals(MainViewModel.Loading.HideSweepRefresh, captor.secondValue)
        kotlin.test.assertEquals(MainViewModel.Loading.ContentLoading(false).isVisible,
                (captor.thirdValue as MainViewModel.Loading.ContentLoading).isVisible)

    }

    @Test
    fun initFailedApiNetworkException() {
        Mockito.`when`(repository.getBusinesses(location = MELBOURNE, limit = LIMIT, category = CATEGORY))
                .thenReturn(Single.error { UnknownHostException() })
        vm.init()

        // on success
        Mockito.verify(message).onChanged("No Network Error")
    }

    @Test
    fun initFailedApiTimeOutException() {
        Mockito.`when`(repository.getBusinesses(location = MELBOURNE, limit = LIMIT, category = CATEGORY))
                .thenReturn(Single.error { SocketTimeoutException() })
        vm.init()

        // on success
        Mockito.verify(message).onChanged("Timeout Error")
    }

    @Test
    fun onViewMapClicked() {
        vm.onViewMapClicked(43.231, 56.78)
        Mockito.verify(navigationEvent).onChanged(MainViewModel.NavigationEvent("http://maps.google.com?q=43.231,56.78"))
    }

    @Test
    fun onRefreshInvoked() {
        val captor = ArgumentCaptor.forClass(MainViewModel.Loading::class.java)

        vm.init()
        vm.onRefreshInvoked()

        Mockito.verify(loading, Mockito.times(6)).onChanged(captor.capture())

        // on subscribe
        kotlin.test.assertEquals(MainViewModel.Loading.ContentLoading(false).isVisible,
                (captor.allValues[3] as MainViewModel.Loading.ContentLoading).isVisible)
    }
}