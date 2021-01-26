package edu.xjtu.social.dao;

import edu.xjtu.social.domain.node.Hobby;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HobbyDao extends Neo4jRepository<Hobby,Long> {
    @Query("match (hobby:Hobby) RETURN hobby")
    List<Hobby> getAllHobbies();

    @Query("create (hobby:Hobby{hname:{hname},htype:{htype}}) return hobby")
    Hobby addHobby(String hname,String htype);

    @Query("match (hobby:Hobby) where ID(hobby)={id} delete hobby")
    Hobby deleteWithId(Long id);

    @Query("match (hobby:Hobby) where ID(hobby)={id} return hobby")
    Hobby getHobbyById(@Param("id") Integer id);

    @Query("match (hobby:Hobby) where ID(hobby)={id} set hobby.hname={hname},hobby.htype={htype} return hobby")
    Hobby fixHobby(@Param("id") Long id,@Param("hname") String hname,@Param("htype") String htype);

    @Query("match (hobby:Hobby) where hobby.hname=~{hname} return hobby")
    List<Hobby> searchHobbyByName(@Param("hname") String hname);

    @Query("match (user:User) where user.account={account}\n" +
            "match ((user)-[:Like]->(hobby:Hobby)) return hobby")
    List<Hobby> getMyHobby(String account);
}