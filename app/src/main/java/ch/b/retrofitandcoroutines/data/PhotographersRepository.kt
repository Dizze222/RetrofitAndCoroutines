package ch.b.retrofitandcoroutines.data

import android.util.Log
import ch.b.retrofitandcoroutines.data.cache.PhotographersCacheDataSource
import ch.b.retrofitandcoroutines.data.cache.PhotographersCacheMapper
import ch.b.retrofitandcoroutines.data.net.PhotographersCloudMapper
import ch.b.retrofitandcoroutines.data.net.PhotographersDataSource

interface PhotographersRepository {
    suspend fun getPhotographers() : PhotographersData

    class Base(
        private val cloudDataSource: PhotographersDataSource,
        private val cacheDataSource: PhotographersCacheDataSource,
        private val photographersCloudMapper: PhotographersCloudMapper,
        private val photographersCacheMapper: PhotographersCacheMapper) : PhotographersRepository{
        override suspend fun getPhotographers() = try {
            val photographerCacheList = cacheDataSource.getPhotographers()
            if (photographerCacheList.isEmpty()){
                Log.i("TAG","Ветка IF сработала")
                val photographerCloudList = cloudDataSource.getPhotographers()
                val photographers = photographersCloudMapper.map(photographerCloudList)
                cacheDataSource.savePhotographers(photographers)
                PhotographersData.Success(photographers)
            }else{
                Log.i("TAG","Ветка ELSE сработала")
                PhotographersData.Success(photographersCacheMapper.map(photographerCacheList))
            }
        }catch (e: Exception){
            PhotographersData.Fail(e)

        }


    }
}