package edu.xjtu.social.domain;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
@Node
@Data
public class BaseNode {
    @Id @GeneratedValue
    protected Long id;
}
