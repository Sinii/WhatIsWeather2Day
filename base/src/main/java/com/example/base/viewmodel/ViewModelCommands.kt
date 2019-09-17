package com.example.base.viewmodel

sealed class ViewModelCommands {
    data class ShowError(val text: String) : ViewModelCommands()
}
