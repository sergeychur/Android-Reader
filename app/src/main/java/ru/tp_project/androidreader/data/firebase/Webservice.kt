package ru.tp_project.androidreader.data.firebase

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.tp_project.androidreader.models.Book

interface Webservice {
    /**
     * @GET declares an HTTP GET request
     * @Path("user") annotation on the userId parameter marks it as a
     * replacement for the {user} placeholder in the @GET path
     */
    @GET("/books/{book}")
    fun getUser(@Path("book") userId: String): Call<Book>
}