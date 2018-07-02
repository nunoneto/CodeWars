package pt.nunoneto.codewars.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NetworkHelper {

    companion object {

        fun getService() : ICodeWarsService {
            return getRetrofit().create(ICodeWarsService::class.java)
        }

        fun getRetrofit() : Retrofit {
            return Retrofit.Builder()
                    .baseUrl("codewars.com/api/v1")
                    .client(getOkHttpClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        fun getOkHttpClient() : OkHttpClient {
            return OkHttpClient.Builder()
                    .addInterceptor {
                        chain ->
                            var request = chain.request()
                                    .newBuilder()
                                    .header("Authorization", "Az4qFgST8S5eVnruacci")
                                    .build()

                            chain.proceed(request) }
                    .build()
        }
    }
}