package com.example.myapplication.internal;

public class LogsPlugin {
        private  String client;
        private  String project;
        private  String userId;

        private LogsPlugin(String client, String project, String userId) {
            this.client = client;
            this.project = project;
            this.userId = userId;
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

        public static final  class Builder{
            private  String client;
            private  String project;
            private  String userId;
            public Builder setClient(String client){
                this.client = client;
                return this;
            }

            public Builder setProject (String project){
                this.project=project;
                return this;
            }

            public Builder setUserId(String userId){
                this.userId=userId;
                return this;
            }

            public LogsPlugin build()
            {
                return new LogsPlugin(client, project, userId);
            }

        }


}
