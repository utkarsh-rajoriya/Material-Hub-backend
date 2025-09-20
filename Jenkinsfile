pipeline {
    agent any

    stages{
        stage('Build'){
            steps{
                withCredentials([file(credentialsId: 'material-hub-envs', variable: 'ENV_FILE')]) {
                    sh '''
                        echo "Copying secret env file..."
                        cp $ENV_FILE target/.env
                        export $(cat target/.env | xargs)
                        mvn clean package -DskipTests
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