pipeline {
    agent any

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
                sh 'nohup java -jar target/*.jar > app.log 2>&1 &'
            }
        }
    }
}
