pipeline {
    agent any
    
    tools {
        maven 'maven'      // Make sure this is configured in Jenkins Global Tool Configuration
        jdk 'java_21'      // Must match JDK installed on EC2 or configured in Jenkins
    }

    stages {
        stage('Build') {
            steps {
                checkout scmGit(
                    branches: [[name: '*/main']],
                    extensions: [],
                    userRemoteConfigs: [[url: 'https://github.com/utkarsh-rajoriya/Material-Hub-backend']]
                )
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Build Docker image & run') {
            steps {
                sh 'docker build -t materialhub .'

                withCredentials([file(credentialsId: 'material-hub-envs', variable: 'ENV_FILE')]) {
                    // stop old container if running
                    sh 'docker stop MaterialHub-container || echo "No running container"'
                    sh 'docker rm MaterialHub-container || echo "No old container"'

                    // run new container in detached mode
                    sh 'docker run -d --name MaterialHub-container --env-file $ENV_FILE -p 8081:8081 materialhub'
                }
            }
        }
    }
}
