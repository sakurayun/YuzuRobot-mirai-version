package cn.yumetsuki.murasame.feature.superadmin

import cn.yumetsuki.murasame.repo.dao.GroupDao
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.getGroupOrNull

fun GroupMessageSubscribersBuilder.sendToGroup() {

    val groupDao: GroupDao by globalKoin().inject()

    val matchRegex = Regex("\\d+ .+")

    superAdmin() and content {
        it.removePrefix("admin ").startsWith("send") && it.removePrefix("admin send ").matches(matchRegex)
    } quoteReply {
        val arguments = it.removePrefix("admin send ").split(" ", limit = 2)
        if (arguments.size != 2) return@quoteReply "唔...参数好像有误呢...示例: admin send 1 发送一条消息～"
        val groupId = arguments[0].toLong()
        val msg = arguments[1]
        groupDao.queryGroupById(groupId)?.let {
            bot.getGroupOrNull(groupId)?.sendMessage(msg)?:"诶？...吾辈好像不在这个群里..."
        }?:"诶？这个群好像没有被授权..."
    }

}