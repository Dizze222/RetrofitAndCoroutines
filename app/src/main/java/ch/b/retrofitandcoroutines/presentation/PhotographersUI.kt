package ch.b.retrofitandcoroutines.presentation

import ch.b.retrofitandcoroutines.R
import ch.b.retrofitandcoroutines.core.Abstract
import ch.b.retrofitandcoroutines.domain.ErrorType
import ch.b.retrofitandcoroutines.domain.PhotographerDomain
import ch.b.retrofitandcoroutines.domain.PhotographerDomainToUIMapper

sealed class PhotographersUI : Abstract.Object<Unit, PhotographerCommunication> {
    class Success(
        private val photographers: List<PhotographerDomain>,
        private val photographerMapper: PhotographerDomainToUIMapper
    ) : PhotographersUI() {
        override fun map(mapper: PhotographerCommunication) {
            val photographersUI = photographers.map {
                it.map(photographerMapper)
            }
            mapper.map(photographersUI)
        }

    }
    class Fail(
        private val errorType: ErrorType,
        private val resourceProvider: ResourceProvider
    ) : PhotographersUI(){
        override fun map(mapper: PhotographerCommunication) {
            val messageId = when(errorType){
                ErrorType.NO_CONNECTION -> R.string.no_connection_message
                ErrorType.SERVICE_UNAVAILABLE -> R.string.service_unavailable
                else -> R.string.something_went_wrong
            }
            val message = resourceProvider.getString(messageId)
            mapper.map(listOf(PhotographerUI.Fail(message)))
        }

    }

    object EmptyData : PhotographersUI() {
        override fun map(mapper: PhotographerCommunication) =
            mapper.map(listOf(PhotographerUI.EmptyData))

    }

}