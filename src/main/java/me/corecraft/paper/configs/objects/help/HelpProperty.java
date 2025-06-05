package me.corecraft.paper.configs.objects.help;

import ch.jalu.configme.Comment;
import ch.jalu.configme.beanmapper.ExportName;

public class HelpProperty {

    @ExportName("results-per-page")
    @Comment("The amount of commands to show per page.")
    public int resultsPerPage = 10;

    @ExportName("primary-color")
    @Comment("The primary color for the color scheme.")
    public String primaryColor = "42,42,0";

    @ExportName("highlight")
    @Comment("The highlight colors.")
    private HighlightProperty highlight = new HighlightProperty();

    @ExportName("text-color")
    @Comment("The color used for description text.")
    public String textColor = "63,63,63";

    @ExportName("accent-color")
    @Comment("The color used for accents and symbols.")
    public String accentColor = "21,63,21";

    public void setResultsPerPage(final int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public void setPrimaryColor(final String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setHighlight(HighlightProperty highlight) {
        this.highlight = highlight;
    }

    public void setAccentColor(final String accentColor) {
        this.accentColor = accentColor;
    }

    public void setTextColor(final String textColor) {
        this.textColor = textColor;
    }

    public HighlightProperty getHighlight() {
        return this.highlight;
    }

    public String getPrimaryColor() {
        return this.primaryColor;
    }

    public String getAccentColor() {
        return this.accentColor;
    }

    public int getResultsPerPage() {
        return this.resultsPerPage;
    }

    public String getTextColor() {
        return this.textColor;
    }
}