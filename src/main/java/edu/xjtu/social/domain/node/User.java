package edu.xjtu.social.domain.node;
import edu.xjtu.social.domain.BaseNode;
import edu.xjtu.social.domain.relationship.Follow;
import edu.xjtu.social.domain.relationship.Like;
import edu.xjtu.social.domain.relationship.Publish;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Node("User")
public class User extends BaseNode {
    @Property(name = "account")
    private String account;
    @Property(name = "password")
    private String password;
    @Property(name = "nickname")
    private String nickname;
    @Property(name = "age")
    private int age;
    @Property(name = "gender")
    private String gender;
    @Property(name = "email")
    private String email;
    @Property(name = "address")
    private String address;
    @Property(name = "imgurl")
    private String imgurl;

    @Relationship(type = "Follow", direction = Direction.OUTGOING)
    private List<Follow> followings;

    @Relationship(type = "Like")
    private List<Like> myhobbys;

    @Relationship(type = "Publish", direction = Direction.OUTGOING)
    private List<Publish> myshares;
}