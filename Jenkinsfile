pipeline {
    agent any

    stages {
        stage('Build & Run') {
            steps {
                withCredentials([file(credentialsId: 'material-hub-envs', variable: 'ENV_FILE')]) {
                    sh '''
                        # Load environment variables
                        export $(grep -v '^#' $ENV_FILE | xargs)
                        echo "Mongo URL: $MONGO_URL"

                        # Build the app
                        mvn clean package -DskipTests

                        # Run the app in background and save logs
                        nohup java -jar target/*.jar \
                            --server.address=0.0.0.0 \
                            --server.port=8081 > app.log 2>&1 &

                        echo "App started, last 50 log lines:"
                        tail -n 50 app.log
                    '''
                }
            }
        }
    }
}
