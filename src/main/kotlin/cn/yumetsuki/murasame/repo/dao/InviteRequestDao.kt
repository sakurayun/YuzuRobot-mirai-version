package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.InviteRequest
import cn.yumetsuki.murasame.repo.entity.InviteRequests
import kotlinx.coroutines.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.map
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.entity.toList

interface InviteRequestDao {

    suspend fun addInviteRequests(vararg inviteRequests: InviteRequest)

    suspend fun deleteInviteRequests(vararg inviteRequests: InviteRequest)

    suspend fun updateInviteRequests(vararg inviteRequests: InviteRequest)

    suspend fun queryInviteRequests(): List<InviteRequest>

}

class InviteRequestDaoImpl(
    private val database: Database
): InviteRequestDao {
    override suspend fun addInviteRequests(vararg inviteRequests: InviteRequest) = withContext(Dispatchers.IO) {
        database.sequenceOf(InviteRequests).let { entitySequence ->
            inviteRequests.map {
                async { entitySequence.add(it) }
            }.awaitAll()
        }
        Unit
    }

    override suspend fun deleteInviteRequests(vararg inviteRequests: InviteRequest) = withContext(Dispatchers.IO) {
        inviteRequests.map {
            async { it.delete() }
        }.awaitAll()
        Unit
    }

    override suspend fun updateInviteRequests(vararg inviteRequests: InviteRequest) = withContext(Dispatchers.IO) {
        inviteRequests.map {
            async { it.flushChanges() }
        }.awaitAll()
        Unit
    }

    override suspend fun queryInviteRequests(): List<InviteRequest> = withContext(Dispatchers.IO) {
        database.sequenceOf(InviteRequests).toList()
    }

}