package me.corecraft.paper.configs.objects.help;

import ch.jalu.configme.Comment;
import ch.jalu.configme.beanmapper.ExportName;

public class HighlightProperty {

    @ExportName("alternative")
    @Comment("The secondary color used to highlight commands and queries.")
    public String highlightColorAlternative = "63,63,21";

    @ExportName("color")
    @Comment("The primary color used to highlight commands and queries.")
    public String highlightColor = "63,21,21";

    public void setHighlightColorAlternative(final String highlightColorAlternative) {
        this.highlightColorAlternative = highlightColorAlternative;
    }

    public void setHighlightColor(final String highlightColor) {
        this.highlightColor = highlightColor;
    }

    public String getHighlightColorAlternative() {
        return this.highlightColorAlternative;
    }

    public String getHighlightColor() {
        return this.highlightColor;
    }
}