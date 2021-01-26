package edu.xjtu.social.dao;
import edu.xjtu.social.domain.node.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendDao extends Neo4jRepository<User,Long> {

    @Query("match (user:User) where user.account={account}\n" +
            "match ((user)-[:Follow]->()-[:Follow]->(p)) return distinct p")
    List<User> byFriend(@Param("account") String account);

    @Query("match (user:User) where user.account={account}\n" +
            "match (user)-[:Like]->(hobbies)\n" +
            "match (users)-[:Like]->(hobbies) return users")
    List<User> byHobby(@Param("account") String account);

    @Query("match (user:User) where user.account={account}\n" +
            "match (user)-[:Publish]->(shares)\n" +
            "match (shares)-[:Praised]->(users) return users")
    List<User> byshare(String account);
}
