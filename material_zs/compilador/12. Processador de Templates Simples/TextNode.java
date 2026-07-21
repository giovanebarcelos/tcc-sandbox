public class TextNode implements TemplateNode {
    private final String text;

    public TextNode(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String describe(String indent) {
        String safe = text
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
        return "Text('" + safe + "')";
    }
}
