package edu.xjtu.social.dao;

import edu.xjtu.social.domain.relationship.Praised;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PraiseDao extends Neo4jRepository<Praised,Long> {

    @Query("match (user:User) where user.account={account} \n" +
            "match (share:Share) where ID(share)={shareid}\n" +
            "create (share)-[praised:Praised]->(user) return ID(praised)")
    Long praisedIt(String account, Integer shareid);

    @Query("match (user:User) where user.account={account} \n" +
            "match (share:Share) where ID(share)={shareid}\n" +
            "match (share)-[praised:Praised]->(user) delete praised return count(praised)")
    Integer canclepraised(String account, Integer shareid);

    @Query("match (share:Share) where ID(share)={shareid}\n" +
            "match (share)-[:Praised]->(user) return count(user)")
    Long getPraisedNumber(Integer shareid);

    @Query("match (share:Share) where ID(share)={shareid}\n" +
            "match (share)-[:Praised]->(user) where user.account={account} return count(user)")
    Integer isPraised(Integer shareid, String account);
}