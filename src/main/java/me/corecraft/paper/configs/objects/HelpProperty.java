package me.corecraft.paper.configs.objects;

import ch.jalu.configme.Comment;
import ch.jalu.configme.beanmapper.ExportName;

public class HelpProperty {

    @ExportName("results-per-page")
    @Comment("The amount of commands to show per page.")
    public int resultsPerPage = 10;

    @ExportName("primary-color")
    @Comment("The primary color for the color scheme.")
    public String primaryColor = "gold";

    @ExportName("highlight.color")
    @Comment("The primary color used to highlight commands and queries.")
    public String highlightColor = "red";

    @ExportName("highlight.alternative")
    @Comment("The secondary color used to highlight commands and queries.")
    public String highlightColorAlternative = "yellow";

    @ExportName("text-color")
    @Comment("The color used for description text.")
    public String textColor = "white";

    @ExportName("accent-color")
    @Comment("The color used for accents and symbols.")
    public String accentColor = "green";

    public void setResultsPerPage(final int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public void setPrimaryColor(final String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setHighlightColor(final String highlightColor) {
        this.highlightColor = highlightColor;
    }

    public void setHighlightColorAlternative(final String highlightColorAlternative) {
        this.highlightColorAlternative = highlightColorAlternative;
    }

    public void setTextColor(final String textColor) {
        this.textColor = textColor;
    }

    public void setAccentColor(final String accentColor) {
        this.accentColor = accentColor;
    }

    public int getResultsPerPage() {
        return this.resultsPerPage;
    }

    public String getPrimaryColor() {
        return this.primaryColor;
    }

    public String getHighlightColor() {
        return this.highlightColor;
    }

    public String getHighlightColorAlternative() {
        return this.highlightColorAlternative;
    }

    public String getTextColor() {
        return this.textColor;
    }

    public String getAccentColor() {
        return this.accentColor;
    }
}