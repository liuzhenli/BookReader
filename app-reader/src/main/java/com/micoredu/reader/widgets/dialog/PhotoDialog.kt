package com.micoredu.reader.widgets.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.request.RequestOptions
import com.microedu.lib.reader.R
import com.microedu.lib.reader.databinding.DialogPhotoViewBinding
import com.micoredu.reader.help.book.BookHelp
import com.micoredu.reader.help.glide.ImageLoader
import com.micoredu.reader.help.glide.OkHttpModelLoader
import com.micoredu.reader.model.BookCover
import com.micoredu.reader.model.ReadBook
import com.micoredu.reader.utils.setLayout

/**
 * 显示图片
 */
class PhotoDialog() : BaseDialogFragment(R.layout.dialog_photo_view) {

    constructor(src: String, sourceOrigin: String? = null) : this() {
        arguments = Bundle().apply {
            putString("src", src)
            putString("sourceOrigin", sourceOrigin)
        }
    }

    private val binding by viewBinding(DialogPhotoViewBinding::bind)

    override fun onStart() {
        super.onStart()
        setLayout(1f, 1f)
    }

    @SuppressLint("CheckResult")
    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let { arguments ->
            arguments.getString("src")?.let { src ->
                val file = ReadBook.book?.let { book ->
                    BookHelp.getImage(book, src)
                }
                if (file?.exists() == true) {
                    ImageLoader.load(requireContext(), file)
                        .error(R.drawable.image_loading_error)
                        .into(binding.photoView)
                } else {
                    ImageLoader.load(requireContext(), src).apply {
                        arguments.getString("sourceOrigin")?.let { sourceOrigin ->
                            apply(
                                RequestOptions().set(
                                    OkHttpModelLoader.sourceOriginOption,
                                    sourceOrigin
                                )
                            )
                        }
                    }.error(BookCover.defaultDrawable)
                        .into(binding.photoView)
                }
            }
        }

    }

}
