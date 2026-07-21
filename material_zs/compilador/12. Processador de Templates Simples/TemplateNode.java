import java.util.List;

public interface TemplateNode {
    String describe(String indent);

    default String describeChildren(String indent, List<TemplateNode> children) {
        if (children == null || children.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String childIndent = indent + "  ";
        for (TemplateNode node : children) {
            sb.append("\n").append(childIndent).append(node.describe(childIndent));
        }
        return sb.toString();
    }
}
