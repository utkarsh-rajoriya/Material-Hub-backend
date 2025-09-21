pipeline {
    agent any
    
    tools{
        maven 'maven_3.9.11'
        jdk 'java_24'
    }

    stages {
        stage('Build') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/utkarsh-rajoriya/Material-Hub-backend']])
                bat 'mvn clean install -DskipTests'
            }
        }
        
        stage('Build Docker image & run'){
            steps {
                bat 'docker build -t materialhub .'
                withCredentials([file(credentialsId: 'material-hub-envs', variable: 'ENV_FILE')]) {
                    bat 'docker stop MaterialHub-container || echo "No running container"'
                    bat 'docker rm MaterialHub-container || echo "No old container"'
                    bat 'docker run -d --name MaterialHub-container --env-file %ENV_FILE% -p 8081:8081 materialhub'
                }
            }
        }
    }
}