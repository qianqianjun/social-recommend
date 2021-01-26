package edu.xjtu.social.domain.node;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import java.util.List;

@Data
@Node("Share")
public class Share {
    @Id
    @GeneratedValue
    private Long id;
    @Property(name = "content")
    private String content; // 发表动态的内容
    @Property(name = "imgurl")
    private String imgurl; // 图片的url地址
    @Property(name = "time")
    private String time; // 发表动态的时间
    @Property(name = "address")
    private String address; // 发表动态的位置
    @Property(name = "title")
    private String title;

    @Property(name = "related_hobby")
    private String relatedHobby; // 相关兴趣

    @Property(name = "hobbyid")
    private Long hobbyid;

    @Property(name = "publisher")
    private String publisher;

    @Property(name = "publisherimg")
    private String publisherimg;

    @Property(name = "publishaccount")
    private String account;

    @Relationship(type = "Praised",direction = Direction.OUTGOING)
    private List<User> praisers;
}