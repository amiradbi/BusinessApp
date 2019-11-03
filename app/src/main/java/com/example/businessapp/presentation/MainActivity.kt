package com.example.businessapp.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.businessapp.R
import com.example.businessapp.application.extensions.getViewModel
import com.example.businessapp.application.extensions.observe
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel

    private val businessAdapter by lazy { BusinessListAdapter(viewModel::onViewMapClicked) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = getViewModel(viewModelFactory)
        viewModel.message.observe(this, onChange = ::showMessage)
        viewModel.viewState.observe(this, onChange = ::renderView)
        viewModel.loading.observe(this, onChange = ::renderLoading)
        viewModel.navigation.observe(this, onChange = ::navigate)

        businessesRecycleView.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = businessAdapter
        }

        swipeContainer.setOnRefreshListener { viewModel.onRefreshInvoked() }

        viewModel.init()
    }

    private fun renderView(state: MainViewModel.ViewState) {
        businessAdapter.updateList(state.businesses)
    }

    private fun renderLoading(loading: MainViewModel.Loading) {
        when (loading) {
            MainViewModel.Loading.HideSweepRefresh -> swipeContainer.isRefreshing = false
            is MainViewModel.Loading.ContentLoading -> {
                if (loading.isVisible) {
                    progressLoading.visibility = View.VISIBLE
                } else {
                    progressLoading.visibility = View.GONE
                }
            }
        }
    }

    private fun navigate(event: MainViewModel.NavigationEvent) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.query))
        startActivity(intent)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}