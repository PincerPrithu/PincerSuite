package me.pincer.lib.database;

public enum QuickDatabaseType {
    MYSQL("MySQL"),
    POSTGRESQL("PostgreSQL"),
    SQLITE("SQLite"),
    MONGODB("MongoDB"),
    REDIS("Redis");

    private final String name;

    QuickDatabaseType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
