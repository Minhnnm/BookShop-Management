package com.example.bookshopmanagement.ui.main.book.author

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookshopmanagement.R
import com.example.bookshopmanagement.data.model.Author
import com.example.bookshopmanagement.data.model.Product
import com.example.bookshopmanagement.data.model.request.AuthorRequest
import com.example.bookshopmanagement.databinding.FragmentAuthorBinding
import com.example.bookshopmanagement.ui.adapter.AuthorAdapter
import com.example.bookshopmanagement.ui.adapter.OnItemClickListener
import com.example.bookshopmanagement.ui.main.book.addbook.AddBookViewModel
import java.io.File
import java.io.FileOutputStream
import java.util.*

class AuthorFragment : Fragment() {

    companion object {
        fun newInstance() = AuthorFragment()
    }

    private lateinit var viewModel: AuthorViewModel
    private lateinit var viewModelAddBook: AddBookViewModel
    private var binding: FragmentAuthorBinding? = null
    private lateinit var adapter: AuthorAdapter
    private var authors = mutableListOf<Author>()
    private var image: String? = null
    private var fileName: String? = null
    private var currentPage = 1
    private var lastPosition = 0
    private var totalPosition = 0
    private var currentPosition = 0
    private var pastPage = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AuthorViewModel::class.java]
        viewModelAddBook = ViewModelProvider(requireActivity())[AddBookViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthorBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AuthorAdapter()
        initViewModel()
        viewModel.getAuthors(10, 1)
        binding?.apply {
            recyclerAuthors.adapter = adapter
            recyclerAuthors.layoutManager = LinearLayoutManager(context)
            linearAddAuthor.setOnClickListener {
                marginTop(R.id.text_list_author, R.id.text_add_author, true)
            }
            textCancel.setOnClickListener {
                marginTop(R.id.text_list_author, R.id.linear_add_author, false)
            }
            textAddAuthor.setOnClickListener {
                marginTop(R.id.text_list_author, R.id.linear_add_author, false)
                parentFragmentManager.popBackStack()
            }
            imageLeft.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            textAddAuthor.setOnClickListener {
                viewModel.checkFields(getData())
                viewModel.getAuthors(10, 1)
                Handler().postDelayed({
                    marginTop(R.id.text_list_author, R.id.linear_add_author, false)
                },300)

            }
        }
        uploadImage()
        selectAuthor()
        handleLoadData()
    }


    private fun initViewModel() {
        viewModel.author.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                currentPage = 1
            } else {
                if (pastPage != currentPage) {
                    if (currentPage > 1) {
                        authors.addAll(it)
                    } else {
                        authors.clear()
                        authors.addAll(it)
                    }
                }
            }
            adapter.setData(authors)
        }
        viewModel.message.observe(viewLifecycleOwner){message->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectAuthor() {
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val author = adapter.getAuthor(position)
                viewModelAddBook.setAuthor(author)
                parentFragmentManager.popBackStack()
            }
        })
    }

    fun getData(): AuthorRequest {
        val name = binding?.editNameAuthor?.text.toString()
        val description = binding?.editDescription?.text.toString()
        return AuthorRequest(
            name = name,
            description = description,
            avatar = image,
            fileName = fileName,
        )
    }
    private fun handleLoadData() {
        binding?.apply {
            recyclerAuthors.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    lastPosition =
                        (recyclerAuthors.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    totalPosition = adapter.itemCount
                    if (lastPosition != currentPosition && lastPosition == totalPosition - 3) {
                        currentPage++
                        viewModel.getAuthors(10, currentPage)
                        currentPosition = lastPosition
                    }
                }
            })
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

            image = base64Image
            fileName = file.name
            binding?.imageAuthor?.setImageURI(uri)
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

    private fun marginTop(view1: Int, view2: Int, isStateAdd: Boolean) {
        if (isStateAdd) {
            binding?.linearAddAuthor?.visibility = View.INVISIBLE
            binding?.groupAddAuthor?.visibility = View.VISIBLE
        } else {
            binding?.linearAddAuthor?.visibility = View.VISIBLE
            binding?.groupAddAuthor?.visibility = View.INVISIBLE
        }
        val constraintSet = ConstraintSet()
        // Sao chép ràng buộc hiện tại từ ConstraintLayout
        constraintSet.clone(binding?.constrainAuthor)

        // Đặt ràng buộc cho View có ID 1 để nằm phía dưới View có ID 2
        constraintSet.connect(
            view1,
            ConstraintSet.TOP,
            view2,
            ConstraintSet.BOTTOM
        )

        // Áp dụng ràng buộc vào ConstraintLayout
        constraintSet.applyTo(binding?.constrainAuthor)
    }
}