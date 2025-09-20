pipeline {
    agent any

    stages{
        stage('Build'){
            steps{
                withCredentials([file(credentialsId: 'material-hub-envs', variable: 'ENV_FILE')]) {
                    def mvnHome = tool name: 'Maven 3.9.2', type: 'maven'
                    sh '''
                        echo "Copying secret env file..."
                        mvn clean package -DskipTests
                        cp $ENV_FILE target/.env
                        export $(cat target/.env | xargs)
                    '''
                }
            }
        }

        stage('Run'){
            steps{
                sh 'java -jar target/*.jar'
            }
        }
    }
}   