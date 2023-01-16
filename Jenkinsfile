#!groovy

pipeline {
    agent any
    stages {
      stage('Maven Install') {
          agent {
          docker {
              image 'maven:3.8.7'
          }
        }
        steps {
          sh 'mvn clean install'
        }
      }
      stage('Docker Build') {
          agent any
        steps {
          sh 'docker build -t hades2004/randomnumber:latest .'
        }
      }
      stage('Docker Push') {
          agent any
        steps {
          withCredentials([usernamePassword(credentialsId: 'docker-credentials', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
              sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
            sh 'docker push hades2004/randomnumber:latest'
          }
        }
      }
      stage('Deploy App') {
        steps {
          withCredentials([string(credentialsId: 'jenkins-token', variable: 'api_token')]) {
             sh 'curl -LO "https://storage.googleapis.com/kubernetes-release/release/v1.20.5/bin/linux/amd64/kubectl"'  
             sh 'chmod u+x ./kubectl'  
             sh './kubectl --token $api_token --server https://host.docker.internal:45375 --insecure-skip-tls-verify=true apply -f deployment2.yaml '
          }
        }
      }
      stage('Update Service') {
        steps {
          withCredentials([string(credentialsId: 'jenkins-token', variable: 'api_token')]) {
             sh './kubectl --token $api_token --server https://host.docker.internal:45375 --insecure-skip-tls-verify=true apply -f service.yaml '
          }
        }
      }      
    }
}