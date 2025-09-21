// pipeline {
//     agent any

//     stages {
//         stage('Build & Run') {
//             steps {
//                 withCredentials([file(credentialsId: 'material-hub-envs', variable: 'ENV_FILE')]) {
//                     sh '''
//                         # Load environment variables
//                         export $(grep -v '^#' $ENV_FILE | xargs)
//                         echo "Mongo URL: $MONGO_URL"

//                         # Build the app
//                         mvn clean package -DskipTests

//                         # Stop any previous instance
//                         if [ -f app.pid ]; then
//                             kill $(cat app.pid) || true
//                             rm -f app.pid
//                         fi

//                         # Run the app in background
//                         nohup java -jar target/*.jar \
//                             --server.address=0.0.0.0 \
//                             --server.port=8081 > app.log 2>&1 &

//                         # Save PID to terminate later
//                         echo $! > app.pid

//                         # Stream logs in real-time
//                         echo "Streaming full logs (terminate after 60s)..."
//                         tail -f app.log &
//                         LOG_PID=$!
//                         sleep 60  # adjust how long you want to see logs
//                         kill $LOG_PID || true

//                         # Stop the app after log streaming
//                         kill $(cat app.pid) || true
//                         rm -f app.pid
//                         echo "App terminated after log streaming."
//                     '''
//                 }
//             }
//         }
//     }
// }
