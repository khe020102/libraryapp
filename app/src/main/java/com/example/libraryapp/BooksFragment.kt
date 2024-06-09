package com.example.libraryapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.libraryapp.BookAdapter
import com.example.libraryapp.R
import com.example.libraryapp.databinding.FragmentBooksBinding

class BooksFragment : Fragment() {

    private lateinit var binding: FragmentBooksBinding
    private lateinit var bookAdapter: BookAdapter

    private val recommendedBooks = listOf(
        Book(R.drawable.r1), Book(R.drawable.r2),
        Book(R.drawable.r3), Book(R.drawable.r4),
        Book(R.drawable.r5), Book(R.drawable.r6),
        Book(R.drawable.r7), Book(R.drawable.r8),
        Book(R.drawable.r9),Book(R.drawable.r10)
    )

    private val popularBooks = listOf(
        Book(R.drawable.b1), Book(R.drawable.b2),
        Book(R.drawable.b3), Book(R.drawable.b4),
        Book(R.drawable.b5), Book(R.drawable.b6),
        Book(R.drawable.b7), Book(R.drawable.b8),
        Book(R.drawable.b9), Book(R.drawable.n11)
    )
    private val newBooks = listOf(
        Book(R.drawable.n1), Book(R.drawable.n2),
        Book(R.drawable.n3), Book(R.drawable.n4),
        Book(R.drawable.n5), Book(R.drawable.n6),
        Book(R.drawable.n7), Book(R.drawable.n8),
        Book(R.drawable.n9), Book(R.drawable.n10)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBooksBinding.inflate(inflater, container, false)

        setupRecyclerView()

        setupSpinner(binding.spinnerFilter)

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        bookAdapter = BookAdapter(popularBooks)
        binding.recyclerView.adapter = bookAdapter
    }

    private fun setupSpinner(spinner: Spinner) {
        val options = listOf("AI 추천도서", "인기도서", "신간도서")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> bookAdapter.updateBooks(recommendedBooks) // AI 추천도서
                    1 -> bookAdapter.updateBooks(popularBooks) // 인기도서
                    2 -> bookAdapter.updateBooks(newBooks) // 신간도서
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }
}
