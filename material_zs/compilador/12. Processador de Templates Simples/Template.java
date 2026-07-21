import java.util.List;

public class Template {
    private final List<TemplateNode> nodes;

    public Template(List<TemplateNode> nodes) {
        this.nodes = nodes;
    }

    public List<TemplateNode> getNodes() {
        return nodes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Template AST:");
        if (nodes != null) {
            for (TemplateNode node : nodes) {
                sb.append("\n  ").append(node.describe("    "));
            }
        }
        return sb.toString();
    }
}
