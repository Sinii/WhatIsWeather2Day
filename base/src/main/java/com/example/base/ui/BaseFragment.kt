package com.example.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.base.viewmodel.BaseViewModel
import com.example.base.viewmodel.ViewModelCommands
import com.example.interfaces.ActivityDefaultBehavior
import com.example.utils.dLog
import com.example.utils.eLog
import com.example.utils.hideKeyboard
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.reflect.KClass


abstract class BaseFragment<B : ViewDataBinding, VMF : ViewModelProvider.Factory>
    : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: VMF

    private var viewModelList: HashSet<BaseViewModel> = HashSet()
    private var firstLaunch = true

    abstract fun provideListOfViewModels(): Array<KClass<*>>

    abstract fun provideActionsBinding(): (B, Set<*>) -> Unit

    abstract fun provideLayout(): Int

    abstract fun provideLifecycleOwner(): Fragment

    open fun onClearSources() {
        viewModelList.forEach { viewModel ->
            viewModel.clearSources()
        }
    }

    open fun busEvents(command: ViewModelCommands, viewModelList: Set<*>, binding: B): Boolean {
        when (command) {
            is ViewModelCommands.ShowError ->
                (provideLifecycleOwner().requireActivity() as ActivityDefaultBehavior).showError(
                    command.text
                )
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            AndroidSupportInjection.inject(provideLifecycleOwner())
        } catch (exception: IllegalArgumentException) {
            exception.printStackTrace()
            "Add Fragment ${provideLifecycleOwner()} into FragmentBuildersModule".eLog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil
            .inflate<B>(
                inflater,
                provideLayout(),
                container,
                false
            )

        this@BaseFragment.hideKeyboard()
        provideListOfViewModels()
            .forEach { viewModelClass ->
                try {
                    @Suppress("UNCHECKED_CAST")
                    ViewModelProvider(this, viewModelFactory)
                        .get(viewModelClass.java as Class<BaseViewModel>)
                        .apply {
                            this.viewModelCommands.observe(
                                provideLifecycleOwner().viewLifecycleOwner,
                                Observer { command ->
                                    if (!busEvents(command, viewModelList, binding)) {
                                        "Do not forget to add super.busEvents()".eLog()
                                    }
                                })
                            if (viewModelList.contains(this)) {
                                firstLaunch = false
                            }
                            viewModelList.add(this)
                        }
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                    "Add ViewModel $viewModelClass into ViewModelFactory".eLog()
                } catch (e: UninitializedPropertyAccessException) {
                    e.printStackTrace()
                }
            }
        "onCreateView provideLifecycleOwner() = ${provideLifecycleOwner()}".dLog()
        "onCreateView provideLifecycleOwner() = ${provideLifecycleOwner().viewLifecycleOwner}".dLog()

        binding.lifecycleOwner = provideLifecycleOwner().viewLifecycleOwner
        binding.apply { provideActionsBinding().invoke(this, viewModelList) }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        "${provideLifecycleOwner().javaClass} onResume".dLog()
        viewModelList.forEach {
            "firstLaunch = $firstLaunch it = $it".dLog()
            if (firstLaunch) it.doAutoMainWork() else it.restoreViewModel()
        }
    }

    override fun onStop() {
        "${provideLifecycleOwner().javaClass} onStop + $this".dLog()
        onClearSources()
        super.onStop()
    }

    override fun onDestroy() {
        "${provideLifecycleOwner().javaClass} onDestroy".dLog()
        viewModelList.clear()
        super.onDestroy()
    }

}

