package com.example.bookshopmanagement.data.repository.author

import com.example.bookshopmanagement.data.model.request.AuthorRequest
import com.example.bookshopmanagement.data.model.response.author.AuthorInfor
import com.example.bookshopmanagement.data.model.response.author.AuthorResponse
import com.example.bookshopmanagement.data.model.response.Message
import retrofit2.Response

interface AuthorRepository {
    suspend fun getAuthor(limit: Int, page: Int): Response<AuthorResponse>

    suspend fun addAuthor(author: AuthorRequest): Response<Message>

    suspend fun getAuthor(authorId: Int): Response<AuthorInfor>?
}