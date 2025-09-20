pipeline {
    agent any

    environment {
        APP_PORT = '8081'
        APP_LOG = 'app.log'
    }

    stages {
        stage('Build') {
            steps {
                withCredentials([file(credentialsId: 'material-hub-envs', variable: 'ENV_FILE')]) {
                    sh '''
                        export $(grep -v '^#' $ENV_FILE | xargs)
                        mvn clean package -DskipTests
                    '''
                }
            }
        }

        stage('Run') {
            steps {
                // Run the app in the background
                sh '''
                    nohup java -jar target/*.jar \
                        --server.address=0.0.0.0 \
                        --server.port=$APP_PORT > $APP_LOG 2>&1 &
                    echo $! > app.pid
                    sleep 5
                '''

                // Stream logs live for 60 seconds
                sh '''
                    echo "Streaming last 50 lines and live logs for 60 seconds..."
                    tail -n 50 -f $APP_LOG &
                    LOG_PID=$!
                    sleep 60
                    kill $LOG_PID || true
                '''
            }
        }
    }

    post {
        always {
            sh '''
                if [ -f app.pid ]; then
                    kill $(cat app.pid) || true
                    rm -f app.pid
                fi
            '''
        }
    }
}
