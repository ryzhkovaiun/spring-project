package entities;

public enum Category {
    Uncategorized("Choose here (optional)", true),
    HighPriority("High priority", false),
    MediumPriority("Medium priority", false),
    LowPriority("Low priority", false),
    NoPriority("No priority", false);

    Category(String fancyName, boolean defaultValue) {
        this.fancyName = fancyName;
        this.defaultValue = defaultValue;
    }

    private final String fancyName;

    private final boolean defaultValue;

    public String getFancyName() {
        return fancyName;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

}
