package com.example.databindingpractise.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BindingFragment<T: ViewBinding>(
    private val bindingCreator: (LayoutInflater, ViewGroup?, Boolean) -> T
): Fragment() {
    private var binding: T? = null

    open fun onBindingCreated(binding: T, savedInstanceState: Bundle?) = Unit

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return bindingCreator(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = binding
        if (binding != null) {
            onBindingCreated(binding, savedInstanceState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}