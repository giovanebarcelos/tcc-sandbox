public class Tag {
    private final String name;
    private final String attr;
    private final int line;
    private final int column;

    public Tag(String name, String attr, int line, int column) {
        this.name = name;
        this.attr = attr;
        this.line = line;
        this.column = column;
    }

    public String getName() {
        return name;
    }

    public String getAttr() {
        return attr;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String renderOpening() {
        if (attr == null || attr.isEmpty()) {
            return "[" + name + "]";
        }
        return "[" + name + "=" + attr + "]";
    }

    public String renderClosing() {
        return "[/" + name + "]";
    }

    @Override
    public String toString() {
        return renderOpening();
    }
}

