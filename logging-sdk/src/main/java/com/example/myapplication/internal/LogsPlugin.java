package com.example.myapplication.internal;

public class LogsPlugin {

    private String client;
    private String project;
    private String userId;
    private String host;
    private int port;
    private long batchSize;

    private LogsPlugin(String client, String project, String userId,
                       String host, int port, long batchSize) {
        this.client = client;
        this.project = project;
        this.userId = userId;
        this.host = host;
        this.port = port;
        this.batchSize = batchSize;
    }
    public String getClient() {
        return client;
    }

    public String getProject() {
        return project;
    }

    public String getUserId() {
        return userId;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public long getBatchSize() {
        return batchSize;
    }

    // ✅ Builder
    public static final class Builder {
        private String client;
        private String project;
        private String userId;
        private String host;
        private int port;
        private long batchSize;

        public Builder setClient(String client) {
            this.client = client;
            return this;
        }

        public Builder setProject(String project) {
            this.project = project;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setBatchSize(long batchSize) {
            this.batchSize = batchSize;
            return this;
        }

        public LogsPlugin build() {

            // 🔥 Optional validations (recommended)
            if (host == null) throw new IllegalArgumentException("Host required");
            if (port <= 0) throw new IllegalArgumentException("Invalid port");
            if (userId == null) throw new IllegalArgumentException("UserId required");
            if (project == null) throw new IllegalArgumentException("Project required");

            return new LogsPlugin(client, project, userId, host, port, batchSize);
        }
    }
}