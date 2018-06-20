package com.jdapplications.jenkins

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object JenkinsInjections {
    fun provideJenkinsRemoteRepository(): JenkinsOverviewRemoteRepository {
        //Return instance of Retrofit with the jenkins service.
        return retrofit.create(JenkinsOverviewRemoteRepository::class.java)
    }

    private val okHttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level
                    .BODY
        }
    }

    private val okHttpClient by lazy { OkHttpClient.Builder().addInterceptor(okHttpLoggingInterceptor).build() }

    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl("https://jenkins.mono-project.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }


}