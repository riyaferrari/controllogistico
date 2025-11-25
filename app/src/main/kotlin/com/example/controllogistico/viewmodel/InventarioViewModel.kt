package com.example.controllogistico.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controllogistico.model.Material
import com.example.controllogistico.repository.InventarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class InventarioViewModel(private val repository: InventarioRepository) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _materiales = repository.materiales

    val materialesVisibles = searchText
        .combine(_materiales) { text, materiales ->
            if (text.isBlank()) {
                materiales
            } else {
                materiales.filter {
                    it.nombre.contains(text, ignoreCase = true) ||
                    it.sku.contains(text, ignoreCase = true)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _materiales.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }
}
