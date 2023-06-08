//Build the Docker Image Using the Dockerfile in the root folder

def buildImage() {
    echo "Build Docker Image with Dockerfile..."
    sh 'docker build -t sivabics/demo-app:nodejs-app .'
}

//Push Docker Image to DockerHub Repository

def pushImage() {
    echo "Pushing Docker Image to Docker Hub Repo..."
    withCredentials([usernamePassword(credentialsId: '3c8dad21-0b06-4a37-88fd-252d1196c345', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push sivabics/demo-app:nodejs-app'
    }
} 
def deployImage() {
    echo "Deploying the application to EC2..."
    def dockerCmd = 'docker run -d -p 80:80 --name react-nodejs-app oluwaseuna/demo-app:react-nodejs-app' 
    sshagent(['ec2-pem-key']) {
        sh "ssh -o StrictHostKeyChecking=no ec2-user@18.130.225.104 ${dockerCmd}"
    }
} 
return this

