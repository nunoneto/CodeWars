package pt.nunoneto.codewars.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkHelper {

    val serviceInstance: ICodeWarsService by lazy { getRetrofit().create(ICodeWarsService::class.java) }

    private fun getRetrofit() : Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://www.codewars.com/api/v1/")
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun getOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor {
                    chain ->
                        val request = chain.request()
                                .newBuilder()
                                .header("Authorization", "Az4qFgST8S5eVnruacci")
                                .build()

                        chain.proceed(request) }
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
    }
}