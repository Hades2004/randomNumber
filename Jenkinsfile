#!groovy

pipeline {
    agent any
    stages {
      stage('Maven Install') {
        agent {
          docker {
            image 'maven:3.9.11-eclipse-temurin-25'
            args '-u root --privileged --network host -v /var/home/bazzite/jenkins_home/.m2:/root/.m2:Z'
          }
        }
        steps {
          sh 'mvn clean install -DskipDownload=true'
          stash name: 'app-jar', includes: 'target/demo.jar'
        }
      }
      stage('Docker Build') {
        steps {
          unstash 'app-jar'
          sh 'DOCKER_BUILDKIT=0 docker build -t docker.io/hades2004/randomnumber:latest .'
        }
      }
      stage('Docker Push') {
        steps {
          withCredentials([usernamePassword(credentialsId: 'docker-credentials', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
            sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword} docker.io"
            sh 'docker push docker.io/hades2004/randomnumber:latest'
          }
        }
      }
      stage('Deploy App') {
        steps {
             sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/v1.20.5/bin/linux/amd64/kubectl"'  
             sh 'chmod u+x ./kubectl'  
             withCredentials([string(credentialsId: 'k8s-deployer-token', variable: 'K8S_TOKEN')]) {
                 sh './kubectl --server https://127.0.0.1:39543 --insecure-skip-tls-verify=true --token=${K8S_TOKEN} apply -f deployment.yaml'
             }
        }
      }
      stage('Update Service') {
        steps {
             withCredentials([string(credentialsId: 'k8s-deployer-token', variable: 'K8S_TOKEN')]) {
                 sh './kubectl --server https://127.0.0.1:39543 --insecure-skip-tls-verify=true --token=${K8S_TOKEN} apply -f service.yaml'
             }
        }
      }      
    }
}