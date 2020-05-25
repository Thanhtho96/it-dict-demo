package com.tt.it_dictionary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tt.it_dictionary.databinding.FragmentImageBinding
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class TabFragmentImage : Fragment() {
    private lateinit var doc: Document
    private val scope = CoroutineScope(SupervisorJob())
    private lateinit var imageView: ImageView
    private lateinit var progressBar: ProgressBar
    private var binding: FragmentImageBinding? = null
    private var imageUrl: String? = null
    private var en: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        en = arguments?.getString("en")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentImageBinding.inflate(inflater, container, false).also {
            binding = it
            imageView = it.image
            progressBar = it.progressBar
            return it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            with(savedInstanceState) {
                imageUrl = getString("imageUrl")
            }
        }
        if (imageUrl == null) {
            scope.launch {
                withContext(Dispatchers.IO) {
                    val url =
                        "https://images.search.yahoo.com/yhs/search;?p=$en+programming&imgsz=large"
                    doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                        .get()
                }
                val images: Elements =
                    doc.select("img")
                if (images.size > 0) {
                    withContext(Dispatchers.Main) {
                        progressBar.visibility = View.GONE
                        imageUrl = images[0].attr("data-src")

                        Glide.with(this@TabFragmentImage)
                            .load(imageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(imageView)
                    }
                }
            }
        } else {
            progressBar.visibility = View.GONE

            Glide.with(this@TabFragmentImage)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            putString("imageUrl", imageUrl)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(en: String) = TabFragmentImage().apply {
            arguments = Bundle().apply {
                putString("en", en)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}