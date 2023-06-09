//Build the Docker Image Using the Dockerfile in the root folder

def buildImage() {
    echo "Build Docker Image with Dockerfile..."
    sh 'ls'
    sh 'docker --version'
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
    def dockerCmd = 'docker run -d -p 3000:5000 --name react-nodejs-app sivabics/demo-app:nodejs-app' 
    sshagent(['awsec2key']) {
        sh "ssh -o StrictHostKeyChecking=no ubuntu@13.51.239.165 ${dockerCmd}"
    }
} 
return this

