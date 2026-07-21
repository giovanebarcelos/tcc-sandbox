public class VariableNode implements TemplateNode {
    private final String name;

    public VariableNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String describe(String indent) {
        return "Variable(" + name + ")";
    }
}
