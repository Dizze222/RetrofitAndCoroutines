package ch.b.retrofitandcoroutines.presentation.certain_post

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import ch.b.retrofitandcoroutines.R
import ch.b.retrofitandcoroutines.core.ImageLoad
import ch.b.retrofitandcoroutines.databinding.FragmentPhotographerDetailBinding
import ch.b.retrofitandcoroutines.presentation.all_posts.PhotographerUI
import androidx.lifecycle.lifecycleScope
import androidx.fragment.app.viewModels
import ch.b.retrofitandcoroutines.presentation.navigate.BackButtonListener
import ch.b.retrofitandcoroutines.presentation.navigate.RouterProvider
import dagger.hilt.android.AndroidEntryPoint
import ch.b.retrofitandcoroutines.presentation.core.BaseFragment


@AndroidEntryPoint
class PhotographerDetailFragment :
    BaseFragment<FragmentPhotographerDetailBinding>(FragmentPhotographerDetailBinding::inflate),
    BackButtonListener {
    private val certainViewModel: CertainPostViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = this.arguments
        val fragment = PhotographerZoomImageFragment()
        val fragmentManager: FragmentManager = activity!!.supportFragmentManager
        var photographerId: Int?
        fragmentManager.setFragmentResultListener("requestKey", this, { key, bundle ->
            photographerId = bundle.getInt("id")
            val adapter = CommentsAdapter()
            val navBar = activity!!.findViewById<View>(R.id.navigation)
            navBar.visibility = View.GONE
            binding.commetsRecyclerView.adapter = adapter
            binding.commetsRecyclerView.layoutManager = GridLayoutManager(activity, 1)
            lifecycleScope.launchWhenStarted {
                certainViewModel.observeCertainPost(this@PhotographerDetailFragment) {
                    it.map { post ->
                        post.map(object : PhotographerUI.StringMapper {
                            override fun map(
                                id: Int,
                                author: String,
                                URL: String,
                                like: Long,
                                theme: String,
                                comments: List<String>,
                                authorOfComments: List<String>
                            ) {
                                binding.idOfAuthor.text = id.toString()
                                binding.author.text = author
                                binding.toolbar.title = author
                                adapter.update(
                                    convertToArrayList(comments),
                                    convertToArrayList(authorOfComments)
                                )
                                ImageLoad.Base(URL).load(binding.imageOfAuthor)
                                bundle.putString("URL", URL)
                            }

                            override fun map(message: String) = Unit

                        })
                    }
                }

            }

            certainViewModel.getCertainPost(photographerId!!.toInt())
        }
        )

        fragment.arguments = bundle
        binding.imageOfAuthor.setOnClickListener {
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }


    fun newInstance(): PhotographerDetailFragment {
        return PhotographerDetailFragment()
    }


    override fun onBackPressed(): Boolean {
        (parentFragment as RouterProvider).router.exit()
        return true
    }
}

fun convertToArrayList(list: List<String>): ArrayList<String> {
    val someList = arrayListOf<String>()
    for (i in list) {
        someList.add(i)
    }
    return someList
}