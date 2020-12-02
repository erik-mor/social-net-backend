package com.vis.moravcik.socialnet.service

import com.vis.moravcik.socialnet.xml.XMLPost
import com.vis.moravcik.socialnet.xml.XMLUser
import com.vis.moravcik.socialnet.xml.XmlWriter
import com.vis.moravcik.socialnet.repository.PostRepository
import com.vis.moravcik.socialnet.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class ArchivingService(
        val userRepository: UserRepository,
        val postRepository: PostRepository,
        val xmlWriter: XmlWriter
) {

    fun archiveUsers(ids: List<Int>){
        // get users that are requested to archiving
        val users = userRepository.getUsersByIds(ids, true)

        // set flag `is_archived` to true in database so users will not more queried for archiving
        userRepository.setArchived(true, ids)

        // getting posts for each user and mapping user to object that will be saved in xml
        val xmlUsers = users.map { XMLUser(it, postRepository.findAllByUserId(it.id).map { post -> XMLPost(post) }) }.toMutableList()

        xmlWriter.saveUsers(xmlUsers)
    }
}