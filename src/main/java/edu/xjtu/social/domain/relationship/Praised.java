package edu.xjtu.social.domain.relationship;

import edu.xjtu.social.domain.node.User;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@RelationshipProperties
public class Praised {
    @Id
    private Long id;
    @TargetNode
    private User user;
}
