package edu.xjtu.social.dao;

import edu.xjtu.social.domain.relationship.Like;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface LikeDao extends Neo4jRepository<Like,Long> {
    @Query("match (user:User) where user.account={account}\n" +
            "match (hobby:Hobby) where ID(hobby)={hobbyid}\n" +
            "create (user)-[like:Like]->(hobby) \n" +
            "create (user)<-[liked:Like]-(hobby) return count(liked)+count(like)")
    Integer likeHobby(String account, Long hobbyid);

    @Query("match (user:User) where user.account={account}\n" +
            "match (hobby:Hobby) where ID(hobby)={hobbyid}\n" +
            "match (user)-[like:Like]-(hobby) delete like return count(like)")
    Integer unlikeHobby(String account, Long hobbyid);
}
