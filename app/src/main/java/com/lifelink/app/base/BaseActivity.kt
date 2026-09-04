package com.lifelink.app.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    protected val bindingOrNull get() = _binding

    abstract fun inflateBinding(inflater: LayoutInflater): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateBinding(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    abstract fun setupUI()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
