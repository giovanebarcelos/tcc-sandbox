import java.util.List;

public class IfNode implements TemplateNode {
    private final String condition;
    private final List<TemplateNode> thenBranch;
    private final List<TemplateNode> elseBranch;

    public IfNode(String condition, List<TemplateNode> thenBranch, List<TemplateNode> elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public String getCondition() {
        return condition;
    }

    public List<TemplateNode> getThenBranch() {
        return thenBranch;
    }

    public List<TemplateNode> getElseBranch() {
        return elseBranch;
    }

    @Override
    public String describe(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append("If(").append(condition).append(") {");
        if (thenBranch != null && !thenBranch.isEmpty()) {
            sb.append("\n").append(indent).append("  THEN:");
            for (TemplateNode node : thenBranch) {
                sb.append("\n").append(indent).append("    ").append(node.describe(indent + "      "));
            }
        }
        if (elseBranch != null && !elseBranch.isEmpty()) {
            sb.append("\n").append(indent).append("  ELSE:");
            for (TemplateNode node : elseBranch) {
                sb.append("\n").append(indent).append("    ").append(node.describe(indent + "      "));
            }
        }
        sb.append("\n").append(indent).append("}");
        return sb.toString();
    }
}
