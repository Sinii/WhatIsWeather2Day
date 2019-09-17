package com.example.base.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.base.R
import com.example.utils.dLog
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {
    @Inject
    lateinit var appContext: Context

    val viewModelCommands = SingleLiveEvent<ViewModelCommands>()
    private var baseViewModelJob = SupervisorJob()
    private val baseViewModelScope = CoroutineScope(Dispatchers.Main + baseViewModelJob)

    abstract fun doAutoMainWork()

    fun <P> doWork(doBlock: suspend CoroutineScope.() -> P): Job =
        doCoroutineWork(doBlock, baseViewModelScope, Dispatchers.IO)

    fun <P> doWorkInMainThread(doBlock: suspend CoroutineScope.() -> P): Job =
        doCoroutineWork(doBlock, baseViewModelScope, Dispatchers.Main)

    open fun restoreViewModel() {}

    override fun onCleared() {
        "onCleared".dLog()
        baseViewModelScope.cancel()
    }

    fun showError(text: String?) {
        if (text != null) {
            viewModelCommands.postValue(ViewModelCommands.ShowError(text))
        }
    }

    private inline fun <P> doCoroutineWork(
        crossinline doBlock: suspend CoroutineScope.() -> P,
        coroutineScope: CoroutineScope,
        context: CoroutineContext
    ): Job {
        return coroutineScope.launch {
            withContext(context) {
                try {
                    doBlock.invoke(this)
                } catch (e: UnknownHostException) {
                    e.printStackTrace()
                    val serverIsUnreachableMessage =
                        appContext.getString(R.string.server_is_unreachable)
                    showError(serverIsUnreachableMessage)
                } catch (e: SocketTimeoutException) {
                    e.printStackTrace()
                    val noInternetConnection = appContext.getString(R.string.no_internet_connection)
                    showError(noInternetConnection)
                } catch (e: ConnectException) {
                    e.printStackTrace()
                    val connectionError = appContext.getString(R.string.connection_error)
                    showError(connectionError)
                }
            }
        }
    }

    open fun clearSources() {

    }
}