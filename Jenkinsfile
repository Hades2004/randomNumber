#!groovy

pipeline {
    agent none
    stages {
      stage('Maven Install') {
          agent {
          docker {
              image 'maven:3.8.7'
          }
        }
        steps {
          sh 'mvn spring-boot:build-image -Dspring-boot.build-image.imageName=hades2004/randomnumber'
        }
      }
/*      
      stage('Docker Build') {
          agent any
        steps {
          sh 'docker build -t hades2004/randomnumber:latest .'
        }
      }
*/
      stage('Docker Push') {
          agent any
        steps {
          withCredentials([usernamePassword(credentialsId: 'docker-credentials', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
              sh "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
            sh 'docker push hades2004/randomnumber:latest'
          }
        }
      }
    }
}