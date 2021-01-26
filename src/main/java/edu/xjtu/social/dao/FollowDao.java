package edu.xjtu.social.dao;

import edu.xjtu.social.domain.node.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FollowDao extends Neo4jRepository<User,Long> {
    @Query("MATCH (a:User),(b:User) WHERE a.account={following} AND b.account={follower} " +
            "CREATE (a)-[follow:Follow]->(b) RETURN ID(follow)")
        // follow 别人
    Long createFollow(@Param("following") String following, @Param("follower") String follower);

    @Query("match (user:User) where user.account={following}\n" +
            "match (user2:User) where user2.account={follower}\n" +
            "match (user)-[r:Follow]->(user2) delete r return count(r)")
        // 不再follow别人
    Long deleteRelation(@Param("following") String following, @Param("follower") String follower);

    @Query("match (me:User) where me.account={myAccount}\n" +
            "match ((me)-[:Follow]->(following)) return distinct following")
        // 查询出我关注的人
    List<User> getMyFollowing(String myAccount);

    @Query("match (me:User) where me.account={myAccount}\n" +
            "match ((me)<-[:Follow]-(follower)) return follower")
        // 查询出关注我的人
    List<User> getPeopleWhoFollowMe(String myAccount);

    @Query("match (me:User) where me.account={myAccount}\n" +
            "match ((me)<-[:Follow]-(follower)) return count(follower)")
        // 查询出多少人关注我
    Long howManyPeopleFollowMe(String myAccount);

    @Query("match (me:User) where me.account={myAccount}\n" +
            "match ((me)-[:Follow]->(following)) return count(following)")
        // 查询出我关注了多少人
    Long howManyIFollow(String myAccount);

    @Query("match (me:User) where me.account={myAccount}\n" +
            "match ((me)-[:Follow]->(friend)-[:Follow]->(me)) return friend")
        // 查询出我的朋友
    List<User> getMyFriends(String myAccount);

    @Query("match (user:User) where user.account={following}\n" +
            "match (user)-[r:Follow]->(user2) where user2.account={follower} return count(user2)")
    Integer isFollow(String following, String follower);
}