pipeline {
    agent any

    stages {
        stage('Build & Run') {
            steps {
                withCredentials([file(credentialsId: 'material-hub-envs', variable: 'ENV_FILE')]) {
                    sh '''
                        # Load environment variables from secret file
                        export $(grep -v '^#' $ENV_FILE | xargs)

                        # Build Spring Boot app
                        mvn clean package -DskipTests

                        # Run the app
                        java -jar target/*.jar &
                    '''
                }
            }
        }
    }
}
