package com.example.bookshopmanagement.ui.main.book.addbook

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.BookShopApp.data.model.Category
import com.example.BookShopApp.data.model.Supplier
import com.example.bookshopmanagement.R
import com.example.bookshopmanagement.data.model.Author
import com.example.bookshopmanagement.data.model.Product
import com.example.bookshopmanagement.data.model.request.ProductRequest
import com.example.bookshopmanagement.databinding.FragmentAddBookBinding
import com.example.bookshopmanagement.ui.main.book.BookViewModel
import com.example.bookshopmanagement.ui.main.book.author.AuthorFragment
import com.example.bookshopmanagement.ui.main.book.author.AuthorViewModel
import com.example.bookshopmanagement.ui.main.book.category.CategoryFragment
import com.example.bookshopmanagement.ui.main.book.publisher.PublisherFragment
import org.w3c.dom.Text
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AddBookFragment() : Fragment() {

    private lateinit var viewModel: AddBookViewModel
    private lateinit var viewModelBook: BookViewModel
    private var binding: FragmentAddBookBinding? = null
    private var author: Author? = null
    private var category: Category? = null
    private var supplier: Supplier? = null
    private var isAddBook: Boolean? = null
    private var fileName: String? = null
    private var uriImage: Uri? = null
    private var imageBook: String? = null
    private var idBook: Int? = null
    private var check = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelBook = ViewModelProvider(this)[BookViewModel::class.java]
        viewModel = ViewModelProvider(requireActivity())[AddBookViewModel::class.java]
        isAddBook = arguments?.getBoolean("isAddBook")
        if (isAddBook == true) {
            viewModel.clear()
        } else {
            viewModel.clearMessage()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBookBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        if (isAddBook == false) {
            binding?.apply {
                textBook.setText(R.string.update_book)
                textAddBook.setText(R.string.update)
                val bookId = arguments?.getSerializable("bookId").toString().toInt()
                if (check) {
                    viewModelBook.getBookDetail(bookId)
                    Log.d("CHECK", check.toString())
                    check = false
                }
            }
        }
        binding?.apply {
            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            editBookAuthor.setOnClickListener {
                viewModel.clearMessage()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, AuthorFragment())
                    .addToBackStack("AddBook").commit()
            }
            editBookCategory.setOnClickListener {
                viewModel.clearMessage()
                val bundle = Bundle()
                bundle.putSerializable("category", category)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, CategoryFragment().apply { arguments = bundle })
                    .addToBackStack("AddBook").commit()
            }
            editBookSupplier.setOnClickListener {
                viewModel.clearMessage()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, PublisherFragment())
                    .addToBackStack("AddBook").commit()
            }
            textAddBook.setOnClickListener {
                val book = getData(author, category, supplier)
                if (checkboxBanner.isChecked) {
                    book.isBannerSelected = true
                }
                isAddBook?.let { isAddBook -> viewModel.checkField(book, isAddBook) }
            }
        }
        uploadImage()
        if (uriImage != null) {
            binding?.imageAvatar?.setImageURI(uriImage)
        } else {
            imageBook?.let {
                binding?.apply {
                    Glide.with(root)
                        .load(imageBook)
                        .centerCrop().into(imageAvatar)
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel.author.observe(viewLifecycleOwner) {
            binding?.editBookAuthor?.setText(it?.authorName)
            author = it
        }
        viewModel.category.observe(viewLifecycleOwner) {
            binding?.editBookCategory?.setText(it?.name)
            category = it
        }
        viewModel.publisher.observe(viewLifecycleOwner) {
            binding?.editBookSupplier?.setText(it?.name)
            supplier = it
        }
        viewModel.message.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
        if (check) {
            viewModelBook.productRequest.observe(viewLifecycleOwner) {
                bindDataUpdate(it)
//                author = it.author
//                category = it.category
//                supplier = it.supplier
                imageBook = it.thumbnail
                viewModel.setCategory(it.category)
                viewModel.setAuthor(it.author)
                viewModel.setSupplier(it.supplier)
                idBook = it.id
            }
        }
    }

    fun getData(
        author: Author?,
        category: Category?,
        supplier: Supplier?,
    ): ProductRequest {
        val name = binding?.editBookName?.text.toString()
        val description = binding?.editDescription?.text.toString()
        var quantity = 0
        if (binding?.editBookQuantity?.text.toString().isNotEmpty()) {
            quantity = binding?.editBookQuantity?.text.toString().toInt()
        }
        var price = "0.0"
        if (binding?.editBookPrice?.text.toString().isNotEmpty()) {
            price = binding?.editBookPrice?.text.toString()
        }
        var discountedPrice = price
        if (binding?.editBookDiscountedprice?.text.toString().isNotEmpty()) {
            discountedPrice = binding?.editBookDiscountedprice?.text.toString()
        }
        return ProductRequest(
            id = idBook,
            name = name,
            description = description,
            quantity = quantity,
            price = price,
            discounted_price = discountedPrice,
            image = imageBook,
            author = author,
            supplier = supplier,
            category = category,
            fileName = fileName
        )
    }

    fun bindData(productRequest: ProductRequest) {
        binding?.apply {
            editBookName.setText(productRequest.name)
            editDescription.setText(productRequest.description)
            editBookQuantity.setText(productRequest.quantity.toString())
            editBookPrice.setText(productRequest.price.toString())
        }
    }

    fun uploadImage() {
        binding?.cardview?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (context?.checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 1)
                } else {
                    openImageDirectory()
                }
            } else {
                if (context?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        1
                    )
                } else {
                    openImageDirectory()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageDirectory()
            } else {
                Toast.makeText(context, "User ko cap quyen", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun openImageDirectory() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*" // Loại tệp tin là ảnh
        }
        startActivityForResult(intent, 1)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            val picturePath = uri?.let { uriToFilePath(it) }
            val file = File(picturePath)
//            val requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//            val multiPart = MultipartBody.Part.createFormData("image", file.name, requestBody)

            val bytes = file.readBytes()
            val base64Image = Base64.getEncoder().encodeToString(bytes)

            imageBook = base64Image
            fileName = file.name
            uriImage = uri
            binding?.imageAvatar?.setImageURI(uri)
        }
    }

    private fun uriToFilePath(uri: Uri): String? {
        val inputStream = context?.contentResolver?.openInputStream(uri)
        inputStream?.use { inputStream ->
            val outputFile = createTempImageFile()
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { outputStream ->
                val buffer = ByteArray(4 * 1024) //4KB
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } >= 0) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                return outputFile.absolutePath
            }
        }
        return null
    }

    private fun createTempImageFile(): File {
        val tempFileName = "temp_image_${System.currentTimeMillis()}.jpg"
        val storageDir = context?.getExternalFilesDir(null)
        return File.createTempFile(tempFileName, ".jpg", storageDir)
    }

    fun bindDataUpdate(book: ProductRequest) {
        binding?.apply {
            Glide.with(root)
                .load(book.thumbnail)
                .centerCrop().into(imageAvatar)
            editBookName.setText(book.name)
            editDescription.setText(book.description)
            editBookPrice.setText(book.price)
            editBookQuantity.setText(book.quantity.toString())
            editBookDiscountedprice.setText(book.discounted_price)
            editBookAuthor.setText(book.author?.authorName)
            editBookCategory.setText(book.category?.name)
            editBookSupplier.setText(book.supplier?.name)
            book.banner?.let {
                checkboxBanner.isChecked = true
            }

        }
    }

//    override fun onDetach() {
//        super.onDetach()
//        if(isAddBook) {
//            viewModel.clear()
//        }
//    }
}