package edu.xjtu.social.domain.relationship;

import edu.xjtu.social.domain.node.User;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

@Data
@RelationshipProperties
public class Follow{
    @Id@GeneratedValue
    private Long id;
    @TargetNode
    private User following;
}