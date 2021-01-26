package edu.xjtu.social.dao;

import edu.xjtu.social.domain.node.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends Neo4jRepository<User,Long> {
    @Query("CREATE (user:User{account:{account},password:{password}," +
            "nickname:{nickname},age:{age},gender:{gender}," +
            "email:{email},address:{address}}) RETURN user")
    // 添加用户
    User adduser(@Param("account") String account,@Param("password") String password,
                 @Param("nickname") String nickname,@Param("age") Integer age,
                 @Param("gender") String gender,@Param("email") String email,
                 @Param("address") String address);

    // 删除用户，注意，删除用户的时候也要所有关联的关系删除掉
    @Query("match (user:User)-[r]-() where user.account={account} delete user,r return user")
    User deleteUserByAccount(@Param("account") String account);

    @Query("MATCH (user:User) WHERE user.account={account} RETURN user")
    // 使用账号获取用户
    User getUserByAccount(@Param("account") String account);

    @Query("MATCH (user:User) WHERE user.account={account} AND user.password={password} RETURN user")
    // 用于登录操纵，查询具有指定用户名和密码的用户
    User checkUser(@Param("account") String account, @Param("password") String password);

    @Query("match (user:User) return user")
    // 得到全部用户
    List<User> getAllUser();

    @Query("match (user:User) where user.account={account} set " +
            "user.email={email},user.age={age},user.address={address},user.nickname={nickname} return count(user)")
    Long fixInfo(String account, String nickname, Integer age, String address, String email);

    @Query("match (user:User) where user.account={account} set user.password={password} return count(user)")
    Long fixPass(String accout,String password);

    @Query("match (user:User) where user.account={account} set user.imgurl={imgurl} return count(user)")
    Long fiximg(String account,String imgurl);
}